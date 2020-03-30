package cn.phlos.paycode.ask.strategy.impl;

import cn.phlos.constant.PayChannelConstant;
import cn.phlos.constant.PayConstant;
import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentChannelEntity;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.paycode.ask.strategy.PayStrategy;
import cn.phlos.paycode.log.AbstractPayment;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.http.HttpClientUtils;
import cn.phlos.util.twitter.SnowflakeIdUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 *
 *
 * @description: 支付宝支付渠道
 */
@Slf4j
@Component
public class AliPayStrategy extends AbstractPayment implements PayStrategy {

    @Override
    public String toPayHtml(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO) {
        log.info(">>>>>支付宝参数封装开始<<<<<<<<");



        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(pymentChannel.getGatewayUrl(), pymentChannel.getMerchantId(),
                pymentChannel.getPrivateKey(), "json", AlipayConfig.charset, pymentChannel.getPublicKey(),
                AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //设置异步回调
        alipayRequest.setReturnUrl(pymentChannel.getAsynUrl());
        //设置同步回调--按照文档为官方服务器进行操作，所以有无都可
        alipayRequest.setNotifyUrl(pymentChannel.getSyncUrl());

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String outTradeNo = paymentTransacDTO.getPaymentId();
        // 付款金额，必填
        String totalAmount = changeF2Y(paymentTransacDTO.getPayAmount() + "");
        // 订单名称，必填
        String subject = "测试项目";
        // 商品描述，可空
        String body = "测试项目";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"total_amount\":\"" + totalAmount
                + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        log.info(">>>>>支付宝参数:{},"+alipayRequest);
        // 请求
        try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public BaseResponse<JSONObject> refund(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO) {


        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(pymentChannel.getGatewayUrl(), pymentChannel.getMerchantId(),
                pymentChannel.getPrivateKey(), "json", AlipayConfig.charset, pymentChannel.getPublicKey(),
                AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no =  paymentTransacDTO.getRefundId();
        //支付宝交易号
        //String trade_no = new String(request.getParameter("WIDTRtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = changeF2Y(paymentTransacDTO.getPayAmount() + "");
        //退款的原因说明
        String refund_reason = "测试说明";
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = paymentTransacDTO.getTradeNo();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                //+ "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");

        //请求
        try {
            AlipayTradeRefundResponse alipayTradeRefundResponse = alipayClient.execute(alipayRequest);
            if (alipayTradeRefundResponse.isSuccess()){

            }
            String result = alipayTradeRefundResponse.getBody();

            //更新订单的信息和日志
            examinePaymentTransaction(out_trade_no,PayConstant.PAY_STATUS_DELETE,null,result,PayChannelConstant.ALI_PAY);


        }  catch (Exception e) {
            e.printStackTrace();
        }

        //输出
       // out.println(result);



        return null;
    }


    /** 金额为分的格式 */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

}