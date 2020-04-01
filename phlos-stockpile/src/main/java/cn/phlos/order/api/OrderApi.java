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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderApi  extends BaseApiService<JSONObject> {

    @Autowired
    private OrderService orderService;


    @GetMapping("/")
    public String test(){
        return "test";
    }

    @GetMapping("/testOrder")
    @ResponseBody
    public String testOrder(String products,Model model){
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(40l);
        orderDto.setOrderItem(1l);
        orderDto.setUserId(1l);
        BaseResponse<JSONObject> jsonObjectBaseResponse = orderService.saveOrder(orderDto);
        if (!isSuccess(jsonObjectBaseResponse)){
            return "500";
        }
        return "redirect:/findOrder";
    }

    @GetMapping("/findOrder")
    public String findOrder(Model model){
        List<OrderDto> orderDtos = orderService.selectOrder();
        model.addAttribute("oderList",orderDtos);
        return "order";
    }

}
