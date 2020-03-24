package cn.phlos.api;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易的信息api提供层
 */

@RestController
public class PaymentTransacInfoApi {

    @Autowired
    private PaymentTransacInfoService payMentTransacInfoService;

    @GetMapping("/tokenByPayMentTransac")
    public BaseResponse<PaymentTransacDTO> tokenByPayMentTransac(String token){
        return payMentTransacInfoService.tokenByPayMentTransac(token);
    }

}
