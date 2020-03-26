package cn.phlos.service.impl;

import cn.phlos.constant.PayConstant;
import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.core.bean.ConvertBeanUtils;
import cn.phlos.util.token.GenerateToken;

import cn.phlos.util.twitter.SnowflakeIdUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        Integer paymentStatus = paymentTransactionEntity.getPaymentStatus();
        if (paymentStatus == PayConstant.PAY_STATUS_SUCCESS || paymentStatus == PayConstant.PAY_STATUS_DELETE){
            return setResultError("该订单已经支付");
        }

        //4.do转dto返回数据给控制层
        return setResultSuccess(ConvertBeanUtils.doToDto(paymentTransactionEntity, PaymentTransacDTO.class));
    }

    @Override
    public BaseResponse<PaymentTransacDTO> refundByPayMent(String payment) {
        //1.查询已支付的信息
        PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectByPaymentId(payment);
        if (paymentTransactionEntity == null){
            return setResultError("未查询到支付信息");
        }
        if (paymentTransactionEntity.getPaymentStatus() == PayConstant.PAY_STATUS_DELETE){
            return setResultError("该订单已经退款");
        }
        if (paymentTransactionEntity.getPaymentStatus() != PayConstant.PAY_STATUS_SUCCESS){
            return setResultError("该订单处于未付款状态");
        }

        //生成新的交易信息
        PaymentTransactionEntity paymentEntity = new PaymentTransactionEntity();
        paymentEntity.setOrderId(paymentTransactionEntity.getOrderId());
        paymentEntity.setPayAmount(paymentTransactionEntity.getPayAmount());
        paymentEntity.setUserId(paymentTransactionEntity.getUserId());
        paymentEntity.setPaymentId(SnowflakeIdUtils.nextId());
        paymentEntity.setPaymentChannel(paymentTransactionEntity.getPaymentChannel());
        paymentEntity.setPaymentStatus(6);
        paymentEntity.setTradeNo(paymentTransactionEntity.getTradeNo());
        paymentEntity.setCreatedTime(new Date());
        paymentTransactionMapper.savePaymentTransaction(paymentEntity);
        PaymentTransacDTO paymentTransacDTO = ConvertBeanUtils.doToDto(paymentEntity, PaymentTransacDTO.class);
        //为了支付宝的订单号
        paymentTransacDTO.setRefundId(paymentTransactionEntity.getPaymentId());
        return setResultSuccess(paymentTransacDTO);
    }
}
