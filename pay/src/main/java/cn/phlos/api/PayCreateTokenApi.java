package cn.phlos.api;

import cn.phlos.dto.input.PayCreateTokenDto;
import cn.phlos.service.PayCreateTokenService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayCreateTokenApi {

    @Autowired
    private PayCreateTokenService payCreateTokenService;

    /**
     * 创建交易的token
     * @param orderId
     * @param payAmount
     * @param userId
     * @return
     */
    @GetMapping("/createPayToken")
    public BaseResponse<JSONObject> createPayToken(String orderId, Long payAmount , Long userId){
        PayCreateTokenDto payCreateTokenDto = new PayCreateTokenDto(payAmount,orderId,userId);
        System.out.println("payCreateTokenDto = " + payCreateTokenDto);
        return payCreateTokenService.createPayToken(payCreateTokenDto);
    }




}
