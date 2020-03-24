package cn.phlos.api;

import cn.phlos.service.PayContextService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayContextApi {

    @Autowired
    private PayContextService payContextService;

    @GetMapping("/toPayHtml")
    public BaseResponse<JSONObject> toPayHtml(@RequestParam("channelId") String channelId,
                                              @RequestParam("payToken") String payToken){

        return payContextService.toPayHtml(channelId,payToken);
    }

}
