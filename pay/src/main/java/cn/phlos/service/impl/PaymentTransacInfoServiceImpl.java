package cn.phlos.service.impl;

import cn.phlos.constant.PayConstant;
import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.order.mapper.OrderMapper;
import cn.phlos.order.mapper.entity.OrderEntity;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.core.bean.ConvertBeanUtils;
import cn.phlos.util.token.GenerateToken;

import cn.phlos.util.twitter.SnowflakeIdUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private OrderMapper orderMapper;
    
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
        if (paymentStatus == PayConstant.PAY_STATUS_SUCCESS || paymentStatus == PayConstant.PAY_STATUS_REFUND){
            return setResultError("该订单已经支付");
        }

        //4.do转dto返回数据给控制层
        return setResultSuccess(ConvertBeanUtils.doToDto(paymentTransactionEntity, PaymentTransacDTO.class));
    }

    @Override
    public BaseResponse<PaymentTransacDTO> refundByOrderId(Long orderId) {
        //1.查询订单信息

        //3.根据订单去查询支付的交易信息
        OrderEntity orderToState = orderMapper.findOrder(orderId);
        //2.判断该订单支付的状态
        if (orderToState == null){
            return setResultError("未查询到支付信息");
        }
        if (!(orderToState.getState() == PayConstant.PAY_STATUS_AGREE_REFUND)){
            return setResultError("该订单正在退款中。。。。");
        }

        //生成新的交易信息
        PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectByOrderIdAndPayment(orderId);
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

    @Override
    public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity) {
        return paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
    }

    @Override
    public int savePaymentTransaction(PaymentTransactionEntity paymentTransactionEntity) {
        return paymentTransactionMapper.savePaymentTransaction(paymentTransactionEntity);
    }

    @Override
    public PaymentTransactionEntity selectById(Long id) {
        return paymentTransactionMapper.selectById(id);
    }

    @Override
    public PaymentTransactionEntity selectByPaymentId(String paymentId) {
        return paymentTransactionMapper.selectByPaymentId(paymentId);
    }

    @Override
    public int updatePaymentStatus(Integer paymentStatus, String paymentId, String tradeNo, String paymentChannel) {
        return paymentTransactionMapper.updatePaymentStatus(paymentStatus,paymentId,tradeNo,paymentChannel);
    }
}
