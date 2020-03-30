package cn.phlos.order.service.impl;

import cn.phlos.order.dto.OrderDto;
import cn.phlos.order.mapper.OrderMapper;
import cn.phlos.order.mapper.entity.OrderEntity;
import cn.phlos.order.service.OrderService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl extends BaseApiService<JSONObject> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Override
    public BaseResponse<JSONObject> saveOrder(OrderDto orderDto) {
        //
        OrderEntity orderEntity = new OrderEntity();
        int orderId = orderMapper.saveOrder(orderEntity);
        JSONObject json = new JSONObject();
        json.put("orderId",orderId);
        return setResultSuccess(json);
    }
}
