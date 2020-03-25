package cn.phlos.service;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.util.base.BaseResponse;

/**
 * 交易的信息服务层
 */
public interface PaymentTransacInfoService {

    public BaseResponse<PaymentTransacDTO> tokenByPayMentTransac(String token);

    public BaseResponse<PaymentTransacDTO>  refundByPayMent(String payment);

}
