package cn.krly.service.payment.provider;

import cn.krly.platform.api.payment.IWechatPay;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.platform.api.payment.pojo.WxPayConfig;
import cn.krly.service.payment.impl.WechatPayImpl;
import cn.krly.service.payment.mapper.WxPayConfigMapper;
import cn.krly.utility.common.HttpsUtils;
import cn.krly.utility.common.RandomUtil;
import cn.krly.utility.common.SerializeUtils;
import cn.krly.utility.common.Utils;
import cn.krly.utility.db.JedisPoolUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


@Service(provider = "WechatPayServiceProviderConfig")
public class WechatPayService implements IWechatPay {
    @Autowired
    private WechatPayImpl wechatPay;

    @Override
    public int createWxPayConfig(int projectId,
                                 String appId,
                                 String appKey,
                                 String mchId,
                                 String notifyUrl,
                                 int type,
                                 String description) {
        return wechatPay.createWxPayConfig(projectId,
                appId,
                appKey,
                mchId,
                notifyUrl,
                type,
                description);
    }

    @Override
    public int updateWxPayConfig(int projectId,
                                 String appId,
                                 String appKey,
                                 String mchId,
                                 String notifyUrl,
                                 int type,
                                 String description) {
        return wechatPay.updateWxPayConfig(projectId,
                appId,
                appKey,
                mchId,
                notifyUrl,
                type,
                description);
    }

    @Override
    public int deleteWxPayConfig(int projectId, int type) {
        return wechatPay.deleteWxPayConfig(projectId, type);
    }

    @Override
    public String payFromH5(int projectId,
                            int name,
                            String orderToken,
                            String userIp,
                            int price,
                            String description) {
        return wechatPay.payFromH5(projectId,
                name,
                orderToken,
                userIp,
                price,
                description);
    }

    @Override
    public String payFromApp(int projectId,
                             int name,
                             String orderToken,
                             String userIp,
                             int price,
                             String description) {
        return wechatPay.payFromApp(projectId,
                name,
                orderToken,
                userIp,
                price,
                description);
    }

    @Override
    public String query(int projectId, int type, String orderToken) {
        return wechatPay.query(projectId, type, orderToken);
    }

    @Override
    public Order getOrderFromCallback(String xml) {
        return wechatPay.getOrderFromCallback(xml);
    }
}
