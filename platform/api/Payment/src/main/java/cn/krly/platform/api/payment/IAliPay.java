package cn.krly.platform.api.payment;


import cn.krly.platform.api.payment.pojo.Order;

import java.util.Map;

public interface IAliPay {
    int createAliPayConfig(int projectId,
                           String appId,
                           String appPrivateKey,
                           String appPublicKey,
                           String returnUrl,
                           String notifyUrl,
                           String gateway,
                           String description);
    int updateAliPayConfig(int projectId,
                           String appId,
                           String appPrivateKey,
                           String appPublicKey,
                           String returnUrl,
                           String notifyUrl,
                           String gateway,
                           String description);
    int deleteAliPayConfig(int projectId);

    String payByApp(int projectId,
                    String productName,
                    String productSubject,
                    String orderToken,
                    int price);
    String payByH5(int projectId,
                   String productName,
                   String productSubject,
                   String orderToken,
                   int price);
    String queryToken(int projectId, String orderToken);

    boolean isValidNotification(int projectId, Map<String, String> paramMap);

    Order getOrderFromCallback(Map<String, String> paramMap);
}
