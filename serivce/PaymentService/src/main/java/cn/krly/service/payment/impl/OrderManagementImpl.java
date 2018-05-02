package cn.krly.service.payment.impl;

import cn.krly.platform.api.payment.IOrderManagement;
import cn.krly.platform.api.payment.pojo.Order;
import cn.krly.service.payment.mapper.OrderMapper;
import cn.krly.utility.common.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OrderManagementImpl implements IOrderManagement {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(int projectId, int productId, int price, int userId, String description) {
        Order order = new Order();
        order.setProjectId(projectId);
        order.setProjectId(productId);
        order.setUserId(userId);
        order.setCents(price);
        order.setDescription(description);
        order.setStatus(Order.UNPAID);

        order.setToken(RandomUtil.getRandomNumber(32));
        order.setCreateTime(new Date());

        //
        orderMapper.insert(order);
        return order;
    }

    @Override
    public boolean isOrderPaid(int orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null)
            return false;

        if (order.getStatus() == Order.PAID)
            return true;
        return false;
    }

    @Override
    public boolean isOrderPaid(String orderToken) {
        Order order = orderMapper.getOrderByToken(orderToken);
        if (order == null)
            return false;

        if (order.getStatus() == Order.PAID)
            return true;
        return false;
    }

    @Override
    public List<Order> getAll(int projectId) {
        List<Order> orderList = orderMapper.selectAllOfProject(projectId);
        return orderList;
    }

    @Override
    public List<Order> getLimit(int projectId, int from, int maxCount) {
        List<Order> orderList = orderMapper.selectOrderOfProject(projectId, from, maxCount);
        return orderList;
    }

    @Override
    public List<Order> getOrdersOfProduct(int projectId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return null;
    }

    @Override
    public int deleteOrder(int orderId) {
        int ret = orderMapper.deleteByPrimaryKey(orderId);
        if (ret > 0)
            return 0;

        return -1;
    }

    @Override
    public int setPaid(String orderToken, boolean isPaid) {
        int status = Order.UNPAID;
        if (isPaid)
            status = Order.PAID;

        int ret = orderMapper.setOrderPaid(orderToken, status);
        if (ret > 0)
            return 0;

        return -1;
    }

    @Override
    public List<Order> getAllOrdersOfUser(int userId) {
        List<Order> orderList = orderMapper.getAllOrdersOfUser(userId);
        return orderList;
    }

    @Override
    public List<Order> getOrdersOfUser(int userId, int productId, int from, int maxCount, Date fromTime, boolean isPaid) {
        return null;
    }

    @Override
    public Order getOrderByToken(String orderToken) {
        return orderMapper.getOrderByToken(orderToken);
    }
}
