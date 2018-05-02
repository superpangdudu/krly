package cn.krly.service.payment.provider;


import cn.krly.platform.api.payment.IOrderManagement;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.impl.OrderManagementImpl;
import cn.krly.service.payment.mapper.OrderMapper;
import cn.krly.utility.common.RandomUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service(provider = "OrderServiceProviderConfig")
public class OrderManagementService implements IOrderManagement {
    @Autowired
    private OrderManagementImpl orderManagement;

    @Override
    public Order createOrder(int projectId, int productId, int price, int userId, String description) {
        return orderManagement.createOrder(projectId, productId, price, userId, description);
    }

    @Override
    public boolean isOrderPaid(int orderId) {
        return orderManagement.isOrderPaid(orderId);
    }

    @Override
    public boolean isOrderPaid(String orderToken) {
        return orderManagement.isOrderPaid(orderToken);
    }

    @Override
    public List<Order> getAll(int projectId) {
        return orderManagement.getAll(projectId);
    }

    @Override
    public List<Order> getLimit(int projectId, int from, int maxCount) {
        return orderManagement.getLimit(projectId, from, maxCount);
    }

    @Override
    public List<Order> getOrdersOfProduct(int projectId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return orderManagement.getOrdersOfProduct(projectId, productId, from, maxCount, fromTime, isPaid);
    }

    @Override
    public int deleteOrder(int orderId) {
        return orderManagement.deleteOrder(orderId);
    }

    @Override
    public int setPaid(String orderToken, boolean isPaid) {
        return orderManagement.setPaid(orderToken, isPaid);
    }

    @Override
    public List<Order> getAllOrdersOfUser(int userId) {
        return orderManagement.getAllOrdersOfUser(userId);
    }

    @Override
    public List<Order> getOrdersOfUser(int userId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return orderManagement.getOrdersOfUser(userId, productId, from, maxCount, fromTime, isPaid);
    }

    @Override
    public Order getOrderByToken(String orderToken) {
        return orderManagement.getOrderByToken(orderToken);
    }
}
