package cn.phlos.api;

import cn.phlos.dto.input.PayCreateTokenDto;
import cn.phlos.service.PayCreateTokenService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "创建令牌服务接口")
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
    @ApiOperation(value = "生成支付token接口")
    public BaseResponse<JSONObject> createPayToken(Long orderId, Long payAmount , Long userId){
        PayCreateTokenDto payCreateTokenDto = new PayCreateTokenDto(payAmount,orderId,userId);
        System.out.println("payCreateTokenDto = " + payCreateTokenDto);
        return payCreateTokenService.createPayToken(payCreateTokenDto);
    }




}
