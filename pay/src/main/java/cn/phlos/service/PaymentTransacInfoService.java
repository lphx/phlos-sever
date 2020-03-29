package cn.phlos.service;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.util.base.BaseResponse;
import org.apache.ibatis.annotations.Param;

/**
 * 交易的信息服务层
 */
public interface PaymentTransacInfoService {

    public BaseResponse<PaymentTransacDTO> tokenByPayMentTransac(String token);

    public BaseResponse<PaymentTransacDTO>  refundByPayMent(String payment);

    public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

    public int savePaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

    public PaymentTransactionEntity selectById(Long id);

    public PaymentTransactionEntity selectByPaymentId(String paymentId);

    public int updatePaymentStatus(Integer paymentStatus,  String paymentId, String tradeNo ,String paymentChannel);

}
