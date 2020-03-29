package cn.phlos.service.impl;

import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;

import cn.phlos.dto.input.PayCreateTokenDto;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.service.PayCreateTokenService;
import cn.phlos.util.token.GenerateToken;
import cn.phlos.util.twitter.SnowflakeIdUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建支付token的实现层
 */
@Service
public class PayCreateTokenServiceImpl extends BaseApiService<JSONObject> implements PayCreateTokenService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private GenerateToken generateToken;

    /**
     * 创建交易的token
     * @param payCreateTokenDto
     * @return
     */
    @Override
    public BaseResponse<JSONObject> createPayToken(PayCreateTokenDto payCreateTokenDto) {
        //1.判断订单，金额，用户id是否为空
        String orderId = payCreateTokenDto.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            return  setResultError("订单号码不能为空!");
        }
        Long payAmount = payCreateTokenDto.getPayAmount();
        if (payAmount == null) {
            return setResultError("金额不能为空!");
        }
        Long userId = payCreateTokenDto.getUserId();
        if (userId == null) {
            return setResultError("userId不能为空!");
        }

        //根据orderId去数据库查找是否有新的订单信息
        PaymentTransactionEntity paymentOrder = paymentTransactionMapper.selectByOrderId(orderId);
        Long payId;
        if (paymentOrder != null){
            payId = paymentOrder.getId();
        }else {
            //2.把记录存进数据库 待支付记录
            PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
            paymentTransactionEntity.setUserId(userId);
            paymentTransactionEntity.setOrderId(orderId);
            paymentTransactionEntity.setPayAmount(payAmount);
            //根据雪花算法生成全局id
            paymentTransactionEntity.setPaymentId(SnowflakeIdUtils.nextId());

            int result = paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
            if (!toDaoResult(result)) {
                return setResultError("系统错误!");
            }
            // 获取主键id
            payId = paymentTransactionEntity.getId();
            if (payId == null) {
                return setResultError("系统错误!");
            }

        }


        //生成对应的支付token
        String keyPrefix = "pay_";
        String token = generateToken.createToken(keyPrefix, payId + "");
        JSONObject dataResult = new JSONObject();
        dataResult.put("token", token);


        return setResultSuccess(dataResult);
    }
}
