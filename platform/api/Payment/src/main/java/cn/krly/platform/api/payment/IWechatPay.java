package cn.krly.platform.api.payment;

import cn.krly.platform.api.payment.pojo.Order;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/26.
 */
public interface IWechatPay {
    int createWxPayConfig(int projectId,
                          String appId,
                          String appKey,
                          String mchId,
                          String notifyUrl,
                          int type,
                          String description);
    int updateWxPayConfig(int projectId,
                          String appId,
                          String appKey,
                          String mchId,
                          String notifyUrl,
                          int type,
                          String description);
    int deleteWxPayConfig(int projectId, int type);

    String payFromH5(int projectId,
                     int name,
                     String orderToken,
                     String userIp,
                     int price,
                     String description);
    String payFromApp(int projectId,
                     int name,
                     String orderToken,
                     String userIp,
                     int price,
                     String description);

    String query(int projectId, int type, String orderToken);

    Order getOrderFromCallback(String xml);
}
