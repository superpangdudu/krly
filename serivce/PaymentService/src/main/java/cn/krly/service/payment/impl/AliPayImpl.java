package cn.krly.service.payment.impl;


import cn.krly.platform.api.payment.IAliPay;
import cn.krly.platform.api.payment.pojo.AliPayConfig;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.mapper.AliPayConfigMapper;
import cn.krly.utility.common.SerializeUtils;
import cn.krly.utility.common.Utils;
import cn.krly.utility.db.JedisPoolUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.Map;

//=======================================================================================
@Component
class AliPayConfigManager {
    @Autowired
    private AliPayConfigMapper mapper;

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
    public AliPayConfig create(int projectId,
                               String appId,
                               String appPrivateKey,
                               String appPublicKey,
                               String returnUrl,
                               String notifyUrl,
                               String gateway,
                               String description) {
        AliPayConfig config = new AliPayConfig();
        config.setProjectId(projectId);
        config.setAppId(appId);
        config.setAppPrivateKey(appPrivateKey);
        config.setAppPublicKey(appPublicKey);
        config.setReturnUrl(returnUrl);
        config.setNotifyUrl(notifyUrl);
        config.setGateway(gateway);
        config.setDescription(description);

        int ret = mapper.insertSelective(config);
        if (ret <= 0)
            return null;

        setToCache(projectId, config);
        return config;
    }

    public AliPayConfig update(int projectId,
                               String appId,
                               String appPrivateKey,
                               String appPublicKey,
                               String returnUrl,
                               String notifyUrl,
                               String gateway,
                               String description) {
        AliPayConfig config = getFromCache(projectId);
        if (config == null) {
            config = mapper.getAliPayConfig(projectId);
            if (config == null)
                return null;
        }

        config.setAppId(appId);
        config.setAppPrivateKey(appPrivateKey);
        config.setAppPublicKey(appPublicKey);
        config.setReturnUrl(returnUrl);
        config.setNotifyUrl(notifyUrl);
        config.setGateway(gateway);
        config.setDescription(description);

        int ret = mapper.updateByPrimaryKeySelective(config);
        if (ret <= 0)
            return null;

        setToCache(projectId, config);
        return config;
    }

    public int delete(int projectId) {
        int ret = mapper.deleteAliPayConfig(projectId);
        if (ret <= 0)
            return -1;

        deleteFromCache(projectId);
        return 0;
    }

    public AliPayConfig get(int projectId) {
        AliPayConfig config = getFromCache(projectId);
        if (config != null)
            return config;

        config = mapper.getAliPayConfig(projectId);
        if (config == null)
            return null;

        setToCache(projectId, config);
        return config;
    }

    //===================================================================================
    private void setToCache(int projectId, AliPayConfig config) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] data = SerializeUtils.serialize(config);
            if (data == null)
                return;

            String key = "alipay - " + projectId;
            String field = "config";

            jedis.hset(key.getBytes(),
                    field.getBytes(),
                    data);
        }
    }

    private AliPayConfig getFromCache(int projectId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "alipay - " + projectId;
            String field = "config";

            byte[] data = jedis.hget(key.getBytes(), field.getBytes());
            AliPayConfig config = SerializeUtils.deserialize(data);

            return config;
        }
    }

    private void deleteFromCache(int projectId) {
        String key = "alipay - " + projectId;
        String field = "config";

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(key, field);
        }
    }
}

//=======================================================================================
@Component
public class AliPayImpl implements IAliPay {
    @Autowired
    private AliPayConfigManager configManager;

    @Autowired
    private OrderManagementImpl orderManagement;

    @Override
    public int createAliPayConfig(int projectId,
                                  String appId,
                                  String appPrivateKey,
                                  String appPublicKey,
                                  String returnUrl,
                                  String notifyUrl,
                                  String gateway,
                                  String description) {
        AliPayConfig aliPayConfig = configManager.create(projectId,
                appId,
                appPrivateKey,
                appPublicKey,
                returnUrl,
                notifyUrl,
                gateway,
                description);

        if (aliPayConfig == null)
            return -1;
        return 0;
    }

    @Override
    public int updateAliPayConfig(int projectId,
                                  String appId,
                                  String appPrivateKey,
                                  String appPublicKey,
                                  String returnUrl,
                                  String notifyUrl,
                                  String gateway,
                                  String description) {
        AliPayConfig aliPayConfig = configManager.update(projectId,
                appId,
                appPrivateKey,
                appPublicKey,
                returnUrl,
                notifyUrl,
                gateway,
                description);

        if (aliPayConfig == null)
            return -1;
        return 0;
    }

    @Override
    public int deleteAliPayConfig(int projectId) {
        return configManager.delete(projectId);
    }

    //===================================================================================
    @Override
    public String payByApp(int projectId,
                           String productName,
                           String productSubject,
                           String orderToken,
                           int price) {
        AliPayConfig config = configManager.get(projectId);
        if (config == null)
            return "";

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(productName);
        model.setSubject(productSubject);
        model.setOutTradeNo(orderToken);
        model.setTotalAmount(Utils.toRMBYuanString(price));
        model.setProductCode("QUICK_MSECURITY_PAY");

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(config.getNotifyUrl());

        try {
            AlipayClient alipayClient = newAlipayClient(config);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String payByH5(int projectId,
                          String productName,
                          String productSubject,
                          String orderToken,
                          int price) {
        AliPayConfig config = configManager.get(projectId);
        if (config == null)
            return "";

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(productName);
        model.setSubject(productSubject);
        model.setOutTradeNo(orderToken);
        model.setTotalAmount(Utils.toRMBYuanString(price));
        model.setProductCode("QUICK_WAP_PAY");

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizModel(model);
        request.setReturnUrl(config.getReturnUrl());
        request.setNotifyUrl(config.getNotifyUrl());

        try {
            AlipayClient alipayClient = newAlipayClient(config);
            return alipayClient.pageExecute(request).getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String queryToken(int projectId, String orderToken) {
        AliPayConfig config = configManager.get(projectId);
        if (config == null)
            return "";

        AlipayClient alipayClient = newAlipayClient(config);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderToken + "\"" +
                "  }");

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean isValidNotification(int projectId, Map<String, String> paramMap) {
        AliPayConfig config = configManager.get(projectId);
        if (config == null)
            return false;

        try {
            return AlipaySignature.rsaCheckV1(paramMap, config.getAppPublicKey(), "utf-8", "RSA2");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Order getOrderFromCallback(Map<String, String> paramMap) {
        String orderToken = paramMap.get("out_trade_no");
        if (Utils.isEmptyString(orderToken))
            return null;

        Order order = orderManagement.getOrderByToken(orderToken);
        if (order == null)
            return null;

        if (isValidNotification(order.getProjectId(), paramMap) == false)
            return null;

        return order;
    }

    //===================================================================================
    private AlipayClient newAlipayClient(AliPayConfig config) {
        return new DefaultAlipayClient(config.getGateway(),
                config.getAppId(), config.getAppPrivateKey(),
                "json", "UTF-8", config.getAppPublicKey(), "RSA2");
    }
}
