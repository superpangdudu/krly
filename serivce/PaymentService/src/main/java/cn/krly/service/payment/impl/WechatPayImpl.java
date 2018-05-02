package cn.krly.service.payment.impl;

import cn.krly.platform.api.payment.IWechatPay;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.platform.api.payment.pojo.WxPayConfig;
import cn.krly.service.payment.mapper.WxPayConfigMapper;
import cn.krly.utility.common.HttpsUtils;
import cn.krly.utility.common.RandomUtil;
import cn.krly.utility.common.SerializeUtils;
import cn.krly.utility.common.Utils;
import cn.krly.utility.db.JedisPoolUtils;
import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


@Component
class WxPayConfigManager {
    @Autowired
    private WxPayConfigMapper mapper;

    @Value("${payment.redis.host}")
    private String redisHost;
    @Value("${payment.redis.port}")
    private int redisPort;

    private JedisPool jedisPool;

    @PostConstruct
    void init() {
        jedisPool = JedisPoolUtils.newJedisPoolInstance(redisHost, redisPort);
    }

    //===================================================================================
    WxPayConfig create(int projectId,
                       String appId,
                       String appKey,
                       String mchId,
                       String notifyUrl,
                       int type,
                       String description) {
        WxPayConfig config = new WxPayConfig();
        config.setProjectId(projectId);
        config.setAppId(appId);
        config.setAppKey(appKey);
        config.setMchId(mchId);
        config.setNotifyUrl(notifyUrl);
        config.setType(type);
        config.setDescription(description);

        int ret = mapper.insertSelective(config);
        if (ret <= 0)
            return null;

        setToCache(projectId, config);
        return config;
    }

    WxPayConfig update(int projectId,
                       int type,
                       String appId,
                       String appKey,
                       String mchId,
                       String notifyUrl,
                       String description) {
        WxPayConfig config = getFromCache(projectId, type);
        if (config == null) {
            config = mapper.getWxPayConfigOfProject(projectId, type);
            if (config == null)
                return null;
        }

        config.setAppId(appId);
        config.setAppKey(appKey);
        config.setMchId(mchId);
        config.setNotifyUrl(notifyUrl);
        config.setDescription(description);

        int ret = mapper.updateByPrimaryKeySelective(config);
        if (ret <= 0)
            return null;

        setToCache(projectId, config);
        return config;
    }

    int delete(int projectId, int type) {
        int ret = mapper.delete(projectId, type);
        if (ret <= 0)
            return -1;

        deleteFromCache(projectId, type);
        return 0;
    }

    WxPayConfig get(int projectId, int type) {
        WxPayConfig config = getFromCache(projectId, type);
        if (config == null) {
            config = mapper.getWxPayConfigOfProject(projectId, type);
            if (config == null)
                return null;
        }

        setToCache(projectId, config);
        return config;
    }

    WxPayConfig getByAppId(String appId) {
        return mapper.getWxPayConfigByAppId(appId);
    }

    //===================================================================================
    private void setToCache(int projectId, WxPayConfig config) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] data = SerializeUtils.serialize(config);
            if (data == null)
                return;

            String key = "wxpay - " + projectId;
            String field = "type - " + config.getType();

            jedis.hset(key.getBytes(),
                    field.getBytes(),
                    data);
        }
    }

    private WxPayConfig getFromCache(int projectId, int type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "wxpay - " + projectId;
            String field = "type - " + type;

            byte[] data = jedis.hget(key.getBytes(), field.getBytes());
            WxPayConfig config = SerializeUtils.deserialize(data);

            return config;
        }
    }

    private void deleteFromCache(int projectId, int type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "wxpay - " + projectId;
            String field = "type - " + type;

            jedis.hdel(key, field);
        }
    }
}

//=======================================================================================
@Component
public class WechatPayImpl implements IWechatPay {
    public static final String WECHAT_PAY_GATEWAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WECHAT_QUERY_GATEWAY = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Autowired
    private WxPayConfigManager configManager;

    @Autowired
    private OrderManagementImpl orderManagement;

    @Override
    public int createWxPayConfig(int projectId,
                                 String appId,
                                 String appKey,
                                 String mchId,
                                 String notifyUrl,
                                 int type,
                                 String description) {
        WxPayConfig config = configManager.create(projectId, appId, appKey, mchId, notifyUrl, type, description);
        if (config == null)
            return -1;
        return 0;
    }

    @Override
    public int updateWxPayConfig(int projectId,
                                 String appId,
                                 String appKey,
                                 String mchId,
                                 String notifyUrl,
                                 int type,
                                 String description) {
        WxPayConfig config = configManager.update(projectId, type, appId, appKey, mchId, notifyUrl, description);
        if (config == null)
            return -1;
        return 0;
    }

    @Override
    public int deleteWxPayConfig(int projectId, int type) {
        return configManager.delete(projectId, type);
    }

    @Override
    public String payFromH5(int projectId,
                            int name,
                            String orderToken,
                            String userIp,
                            int price,
                            String description) {
        WxPayConfig config = configManager.get(projectId, WxPayConfig.TYPE_H5);
        if (config == null)
            return "";

        Map<String, String> paramMap = getParamMap(config.getAppId(),
                config.getMchId(),
                config.getNotifyUrl(),
                userIp,
                "MWEB",
                orderToken,
                price,
                description);

        final String signInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_name\": \"凯叡隆誉\",\"wap_url\": \"krly.cn\"}}";
        paramMap.put("scene_info", signInfo);

        String sign = getSign(paramMap, config.getAppKey());
        paramMap.put("sign", sign);

        //
        String xmlContent = toXML(paramMap);

        //
        byte[] bytes = HttpsUtils.post(WECHAT_PAY_GATEWAY, xmlContent);
        String response = new String(bytes);

        //
        paramMap = fromXML(response);
        String returnCode = paramMap.get("return_code");
        if (Utils.isEmptyString(returnCode))
            return "";

        if (returnCode.equals("SUCCESS") == false)
            return returnCode;

        //
        String resultCode = paramMap.get("result_code");
        if (Utils.isEmptyString(resultCode))
            return "";

        if (resultCode.equals("SUCCESS") == false)
            return returnCode;

        return paramMap.get("mweb_url");
    }

    @Override
    public String payFromApp(int projectId,
                             int name,
                             String orderToken,
                             String userIp,
                             int price,
                             String description) {
        WxPayConfig config = configManager.get(projectId, WxPayConfig.TYPE_APP);
        if (config == null)
            return "";

        Map<String, String> paramMap = getParamMap(config.getAppId(),
                config.getMchId(),
                config.getNotifyUrl(),
                userIp,
                "APP",
                orderToken,
                price,
                description);

        String sign = getSign(paramMap, config.getAppKey());
        paramMap.put("sign", sign);

        //
        String xmlContent = toXML(paramMap);

        //
        byte[] bytes = HttpsUtils.post(WECHAT_PAY_GATEWAY, xmlContent);
        String response = new String(bytes);

        //
        paramMap = fromXML(response);
        String returnCode = paramMap.get("return_code");
        if (Utils.isEmptyString(returnCode))
            return "";

        if (returnCode.equals("SUCCESS") == false)
            return returnCode;

        //
        String resultCode = paramMap.get("result_code");
        if (Utils.isEmptyString(resultCode))
            return "";

        //
        String prepayId = paramMap.get("prepay_id");
        if (Utils.isEmptyString(prepayId))
            return "PREPAY_ID Failed";

        //
        Map<String, String> resultMap = new TreeMap<>();
        String noncestr = RandomUtil.getRandomString(32);
        String timestamp = System.currentTimeMillis() + "";
        timestamp = timestamp.substring(0, 10);

        resultMap.put("appid", config.getAppId());
        resultMap.put("partnerid", config.getMchId());
        resultMap.put("prepayid", prepayId);
        resultMap.put("package", "WXPay");
        resultMap.put("noncestr", noncestr);
        resultMap.put("timestamp", timestamp);

        sign = getSign(resultMap, config.getAppKey());
        resultMap.put("sign", sign);

        String result = JSON.toJSONString(resultMap);
        return result;
    }

    @Override
    public String query(int projectId, int type, String orderToken) {
        WxPayConfig config = configManager.get(projectId, type);
        if (config == null)
            return "Failed";

        String appid = config.getAppId();
        String mch_id = config.getMchId();
        String nonce_str = RandomUtil.getRandomString(32);
        String out_trade_no = orderToken;

        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put("appid", appid);
        paramMap.put("mch_id", mch_id);
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("out_trade_no", out_trade_no);

        String appKey = config.getAppKey();
        String sign = getSign(paramMap, appKey);
        paramMap.put("sign", sign);

        //
        String content = toXML(paramMap);

        byte[] bytes = HttpsUtils.post(WECHAT_QUERY_GATEWAY, content);
        String response = new String(bytes);

        //
        paramMap = fromXML(response);
        String return_code = paramMap.get("return_code");
        if (Utils.isEmptyString(return_code))
            return "";

        if (return_code.equals("SUCCESS") == false)
            return return_code;

        //
        String result_code = paramMap.get("result_code");
        if (Utils.isEmptyString(result_code))
            return "";

        if (result_code.equals("SUCCESS") == false)
            return return_code;

        //
        return paramMap.get("trade_state");
    }

    @Override
    public Order getOrderFromCallback(String xml) {
        Map<String, String> paramMap = fromXML(xml);
        if (paramMap == null
                || paramMap.keySet().size() == 0)
            return null;

        if (isValidSign(paramMap) == false)
            return null;

        String resultCode = paramMap.get("result_code");
        String returnCode = paramMap.get("return_code");

        if (resultCode.equals("SUCCESS") == false
                || returnCode.equals("SUCCESS") == false)
            return null;

        String orderToken = paramMap.get("out_trade_no");
        Order order = orderManagement.getOrderByToken(orderToken);
        return order;
    }

    //===================================================================================
    private Map<String, String> getParamMap(String appId,
                                            String mch_id,
                                            String notify_url,
                                            String userIp,
                                            String trade_type,
                                            String orderToken,
                                            int price,
                                            String description) {
        String nonce_str = RandomUtil.getRandomString(32);
        String body = description;
        String out_trade_no = orderToken;
        String spbill_create_ip = userIp;
        String total_fee = Integer.toString(price);

        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put("notify_url", notify_url);
        paramMap.put("appid", appId);
        paramMap.put("mch_id", mch_id);
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("body", body);
        paramMap.put("out_trade_no", out_trade_no);
        paramMap.put("spbill_create_ip", spbill_create_ip);
        paramMap.put("total_fee", total_fee);
        paramMap.put("trade_type", trade_type);

        return paramMap;
    }

    private boolean isValidSign(Map<String, String> paramMap) {
        String appId = paramMap.get("appid");
        WxPayConfig config = configManager.getByAppId(appId);
        if (config == null)
            return false;

        return isValidSign(paramMap, config.getAppKey());
    }

    private boolean isValidSign(Map<String, String> paramMap, String appKey) {
        String sign = paramMap.get("sign");
        if (Utils.isEmptyString(sign))
            return false;

        String expectedSign = getSign(paramMap, appKey);
        if (expectedSign.equals(sign))
            return true;

        return false;
    }

    private String getSign(Map<String, String> paramMap, String appKey) {
        StringBuilder sb = new StringBuilder();
        int pos = 0;

        for (String key : paramMap.keySet()) {
            if (key.equals("sign"))
                continue;

            String value = paramMap.get(key);
            if (Utils.isEmptyString(value))
                continue;

            if (pos > 0)
                sb.append("&");

            sb.append(key);
            sb.append("=");
            sb.append(value);

            ++pos;
        }

        sb.append("&key=").append(appKey);

        String md5 = Utils.MD5(sb.toString()).toUpperCase();
        return md5;
    }

    private String toXML(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");

        for (String key : paramMap.keySet()) {
            sb.append("<");
            sb.append(key);
            sb.append("><![CDATA[");

            sb.append(paramMap.get(key));

            sb.append("]]></");
            sb.append(key);
            sb.append(">");
        }

        sb.append("</xml>");

        return sb.toString();
    }

    private Map<String, String> fromXML(String xml) {
        Map<String, String> paramMap = new TreeMap<>();

        try {
            Document document = DocumentHelper.parseText(xml);
            Element element = document.getRootElement();

            Iterator iterator = element.elementIterator();
            while (iterator.hasNext()) {
                Element item = (Element) iterator.next();

                paramMap.put(item.getName(), item.getStringValue());
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return paramMap;
    }


}
