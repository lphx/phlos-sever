package cn.phlos.order.api;

import cn.phlos.order.dto.OrderDto;
import cn.phlos.order.service.OrderService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderApi  extends BaseApiService<JSONObject> {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String test(){
        return "test";
    }

    @GetMapping("/testOrder")
    public String testOrder(Model model){
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(40l);
        orderDto.setOrderItem(1l);
        orderDto.setUserId(1l);
        BaseResponse<JSONObject> jsonObjectBaseResponse = orderService.saveOrder(orderDto);
        if (!isSuccess(jsonObjectBaseResponse)){
            return "500";
        }
        JSONObject data = jsonObjectBaseResponse.getData();
        model.addAttribute("orderId",data.get("orderId"));
        model.addAttribute("order",orderDto);
        return "order";
    }

}
