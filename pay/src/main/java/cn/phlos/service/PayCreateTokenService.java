package cn.phlos.service;

import cn.phlos.util.base.BaseResponse;

import cn.phlos.dto.input.PayCreateTokenDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.validation.annotation.Validated;

/**
 * 创建支付token的服务层
 */
public interface PayCreateTokenService {


    public BaseResponse<JSONObject> createPayToken(PayCreateTokenDto payCreateTokenDto);

}
