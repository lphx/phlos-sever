package cn.phlos.api;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易的信息api提供层
 */

@RestController
@Api(tags = "交易记录服务接口")
public class PaymentTransacInfoApi {

    @Autowired
    private PaymentTransacInfoService payMentTransacInfoService;

    @GetMapping("/tokenByPaymentTransac")
    @ApiOperation("使用oken获取支付参数接口")
    public BaseResponse<PaymentTransacDTO> tokenByPaymentTransac(String token){
        return payMentTransacInfoService.tokenByPayMentTransac(token);
    }

}
