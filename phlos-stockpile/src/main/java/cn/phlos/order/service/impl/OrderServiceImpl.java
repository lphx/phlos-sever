package cn.phlos.order.service.impl;

import cn.phlos.order.dto.OrderDto;
import cn.phlos.order.mapper.OrderMapper;
import cn.phlos.order.mapper.entity.OrderEntity;
import cn.phlos.order.service.OrderService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseApiService<JSONObject> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Override
    public BaseResponse<JSONObject> saveOrder(OrderDto orderDto) {
        //
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(orderDto.getUserId());
        orderEntity.setAmount(orderDto.getAmount());
        orderEntity.setState(0);
        orderEntity.setCreateBy(orderDto.getUserId()+"");
        int orderId = orderMapper.saveOrder(orderEntity);
        JSONObject json = new JSONObject();
        json.put("orderId",orderEntity.getId());
        return setResultSuccess(json);
    }

    @Override
    public OrderEntity findOrderToState(Long orderId) {
        return orderMapper.findOrder(orderId);
    }

    @Override
    public int updateOrderPaymentState(Long transactionId, Integer state, String updateBy, Long orderId) {
        return updateOrderPaymentState(transactionId,state,updateBy,orderId);
    }

    @Override
    public List<OrderDto> selectOrder() {
        return orderMapper.selectOrder();
    }


}
