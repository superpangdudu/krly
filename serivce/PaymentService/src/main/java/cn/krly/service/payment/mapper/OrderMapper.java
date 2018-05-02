package cn.krly.service.payment.mapper;

import cn.krly.platform.api.payment.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Order record);
    int insertSelective(Order record);
    Order selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Order record);
    int updateByPrimaryKey(Order record);

    Order getOrderByToken(String orderToken);
    List<Order> selectAllOfProject(int projectId);
    List<Order> selectOrderOfProject(int projectId, int from, int maxCount);
    int setOrderPaid(String token, int status);
    List<Order> getAllOrdersOfUser(int userId);
}