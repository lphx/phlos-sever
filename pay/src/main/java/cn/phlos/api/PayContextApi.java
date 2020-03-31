package cn.phlos.api;

import cn.phlos.service.PayContextService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "交易上下文的接口")
public class PayContextApi {

    @Autowired
    private PayContextService payContextService;

    @GetMapping("/toPayHtml")
    @ApiOperation(value = "获取对应支付渠道交易信息接口")
    public BaseResponse<JSONObject> toPayHtml(@RequestParam("channelId") String channelId,
                                              @RequestParam("payToken") String payToken){

        return payContextService.toPayHtml(channelId,payToken);
    }

}
