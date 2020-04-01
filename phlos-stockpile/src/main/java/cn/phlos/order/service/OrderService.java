package cn.phlos.order.service;

import cn.phlos.order.dto.OrderDto;
import cn.phlos.order.mapper.entity.OrderEntity;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderService {

    public BaseResponse<JSONObject> saveOrder(OrderDto orderDto);

    public OrderEntity findOrderToState(Long orderId);

    public int updateOrderPaymentState(Long transactionId, Integer state, String updateBy, Long orderId);

    public List<OrderDto> selectOrder();
}
