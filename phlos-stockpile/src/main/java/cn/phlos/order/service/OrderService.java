package cn.phlos.order.service;

import cn.phlos.order.dto.OrderDto;
import cn.phlos.order.mapper.entity.OrderEntity;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.Order;

public interface OrderService {

    public BaseResponse<JSONObject> saveOrder(OrderDto orderDto);

}
