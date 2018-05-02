package cn.krly.platform.api.payment;

import cn.krly.platform.api.payment.pojo.Order;

import java.util.Date;
import java.util.List;


public interface IOrderManagement {
    Order createOrder(int projectId,
                      int productId,
                      int price,
                      int userId,
                      String description);
    boolean isOrderPaid(int orderId);

    boolean isOrderPaid(String orderToken);

    /**
     *
     * @param projectId , -1表示全部项目
     * @return
     */
    List<Order> getAll(int projectId);

    /**
     *
     * @param projectId , -1表示全部项目
     * @param from
     * @param maxCount
     * @return
     */
    List<Order> getLimit(int projectId, int from, int maxCount);

    /**
     *
     * @param projectId , -1表示全部项目
     * @param productId ，-1表示全部产品
     * @param from ，起始偏移
     * @param maxCount ，最大条数
     * @param fromTime ，起始时间
     * @param isPaid ，true - 已支付，false - 未支付，
     * @return
     */
    List<Order> getOrdersOfProduct(int projectId, int productId, int from, int maxCount, Date fromTime, boolean isPaid);

    int deleteOrder(int orderId);
    int setPaid(String orderToken, boolean isPaid);

    List<Order> getAllOrdersOfUser(int userId);

    /**
     *
     * @param userId , -1表示全部用户
     * @param productId ， -1表示全部产品
     * @param from ，起始偏移
     * @param maxCount ，最大条数
     * @param fromTime ，起始时间
     * @param isPaid ，true - 已支付，false - 未支付，
     * @return
     */
    List<Order> getOrdersOfUser(int userId, int productId, int from, int maxCount, Date fromTime, boolean isPaid);

    Order getOrderByToken(String orderToken);
}
