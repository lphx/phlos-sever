package cn.phlos.paycode.ask.strategy;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.entity.PaymentChannelEntity;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;

/**
 * 支付接口共同实现行为算法
 */
public interface PayStrategy {


    /**
     * 支付
     * @param pymentChannel
     *            渠道参数
     * @param paymentTransacDTO
     *            支付参数
     * @return
     */
    public String toPayHtml(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO);

    /**
     *退货
     * @param pymentChannel
     *            渠道参数
     * @param paymentTransacDTO
     *            支付参数
     * @return
     */
    public BaseResponse<JSONObject> refund(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO);


}
