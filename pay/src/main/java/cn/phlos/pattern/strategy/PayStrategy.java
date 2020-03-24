package cn.phlos.pattern.strategy;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.entity.PaymentChannelEntity;

/**
 * 支付接口共同实现行为算法
 */
public interface PayStrategy {


    /**
     *
     * @param pymentChannel
     *            渠道参数
     * @param paymentTransacDTO
     *            支付参数
     * @return
     */
    public String toPayHtml(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO);

}
