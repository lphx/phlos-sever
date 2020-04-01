package cn.phlos.product.api;

import cn.phlos.product.dto.CarDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShoppingApi {

    @GetMapping("/addShopping")
    @ResponseBody
    public  String  addShopping(Long  productId, Integer qty, String name , BigDecimal price,String image ,HttpServletRequest request){
        CarDto carDto = new CarDto();
        carDto.setProductId(productId);
        carDto.setQty(qty);
        carDto.setName(name);
        carDto.setPrice(price);
        carDto.setImage(image);
        HttpSession session = request.getSession();
        List<CarDto> carSession = (List<CarDto>) session.getAttribute("1");
        if (carSession == null){
            carSession = new ArrayList<>();
        }
        carSession.add(carDto);
        session.setAttribute("1",carSession);
        return "ok";
    }

    @GetMapping("/findShopping")
    public String findShopping(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        List<CarDto> carSession = (List<CarDto>) session.getAttribute("1");
        model.addAttribute("carList",carSession);
        return "car_list";
    }

}
