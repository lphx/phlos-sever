package cn.phlos.service.impl;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.core.bean.ConvertBeanUtils;
import cn.phlos.util.token.GenerateToken;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易的信息实现层
 */

@Service
public class PaymentTransacInfoServiceImpl extends BaseApiService<PaymentTransacDTO> implements PaymentTransacInfoService {
    
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;
    
    @Override
    public BaseResponse<PaymentTransacDTO> tokenByPayMentTransac(String token) {
        //1.判断token是否为空
        if (StringUtils.isEmpty(token)){
            return setResultError("token不能为空");
        }
        //2.从Redis取出token
        String value = generateToken.getToken(token);
        if (StringUtils.isEmpty(value)){
            return setResultError("该Token可能已经失效或者已经过期");
        }
        //3.通过id去数据库获取当前交易信息
        PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectById(Long.parseLong(value));
        if(paymentTransactionEntity == null){
            return setResultError("未查询到支付信息");
        }
        //4.do转dto返回数据给控制层
        return setResultSuccess(ConvertBeanUtils.doToDto(paymentTransactionEntity, PaymentTransacDTO.class));
    }
}
