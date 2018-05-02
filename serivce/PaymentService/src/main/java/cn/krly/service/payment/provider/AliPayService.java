package cn.krly.service.payment.provider;

import cn.krly.platform.api.payment.IAliPay;

import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.impl.AliPayImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

//=======================================================================================
@Service(provider = "AliPayServiceProviderConfig")
public class AliPayService implements IAliPay {
    @Autowired
    private AliPayImpl aliPayImpl;

    @Override
    public int createAliPayConfig(int projectId,
                                  String appId,
                                  String appPrivateKey,
                                  String appPublicKey,
                                  String returnUrl,
                                  String notifyUrl,
                                  String gateway,
                                  String description) {
        return aliPayImpl.createAliPayConfig(projectId,
                appId,
                appPrivateKey,
                appPublicKey,
                returnUrl,
                notifyUrl,
                gateway,
                description);
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
        return aliPayImpl.updateAliPayConfig(projectId,
                appId,
                appPrivateKey,
                appPublicKey,
                returnUrl,
                notifyUrl,
                gateway,
                description);
    }

    @Override
    public int deleteAliPayConfig(int projectId) {
        return aliPayImpl.deleteAliPayConfig(projectId);
    }

    @Override
    public String payByApp(int projectId,
                           String productName,
                           String productSubject,
                           String orderToken,
                           int price) {
        return aliPayImpl.payByApp(projectId,
                productName,
                productSubject,
                orderToken,
                price);
    }

    @Override
    public String payByH5(int projectId,
                          String productName,
                          String productSubject,
                          String orderToken,
                          int price) {

        return aliPayImpl.payByH5(projectId,
                productName,
                productSubject,
                orderToken,
                price);
    }

    @Override
    public String queryToken(int projectId, String orderToken) {
        return aliPayImpl.queryToken(projectId, orderToken);
    }

    @Override
    public boolean isValidNotification(int projectId, Map<String, String> paramMap) {
        return aliPayImpl.isValidNotification(projectId, paramMap);
    }

    @Override
    public Order getOrderFromCallback(Map<String, String> paramMap) {
       return aliPayImpl.getOrderFromCallback(paramMap);
    }
}
