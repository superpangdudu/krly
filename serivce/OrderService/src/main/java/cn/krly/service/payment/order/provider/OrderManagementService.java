package cn.krly.service.payment.order.provider;


import cn.krly.platform.api.payment.IOrderManagement;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.order.mapper.OrderMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class OrderManagementService implements IOrderManagement {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(int projectId, int productId, int price, int userId, String description) {
        Order order = new Order();
        order.setProjectId(projectId);

        return null;
    }

    @Override
    public boolean isOrderPaid(int orderId) {
        return false;
    }

    @Override
    public List<Order> getAll(int projectId) {
        return null;
    }

    @Override
    public List<Order> getLimit(int projectId, int from, int maxCount) {
        return null;
    }

    @Override
    public List<Order> getOrdersOfProduct(int projectId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return null;
    }

    @Override
    public int deleteOrder(int orderId) {
        return 0;
    }

    @Override
    public int setPaid(int orderId, boolean isPaid) {
        return 0;
    }

    @Override
    public List<Order> getAllOrdersOfUser(int userId) {
        return null;
    }

    @Override
    public List<Order> getOrdersOfUser(int userId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return null;
    }
}
