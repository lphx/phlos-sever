package cn.phlos.paycode.callback.template.impl;

import cn.phlos.constant.PayChannelConstant;
import cn.phlos.constant.PayConstant;
import cn.phlos.mapper.PaymentChannelMapper;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.paycode.callback.template.AbstractPayCallbackTemplate;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class AliPayCallbackTemplate extends AbstractPayCallbackTemplate {

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) throws AlipayApiException {

        log.info(">>>>>>>>>>支付宝接收后台通知开始<<<<<<<<<<");
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = req.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }
        //==================同步回调的代码=======================================
        String trade_status = req.getParameter("trade_status");
        if (trade_status != null){

        }
        //==================异步回调的代码=======================================
        String publicKey = paymentChannelMapper.selectBychannelId(PayChannelConstant.ALI_PAY).getPublicKey();
        boolean signVerified = AlipaySignature.rsaCheckV1(params, publicKey, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            log.info(">>>>>>>>>>支付宝接收后台通知:{},SDK验签成功");
            //获取到交易的id和赋值状态
            String paymentId = req.getParameter("out_trade_no");
            params.put("paymentId", paymentId);
            params.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_200);
        }else {
            log.info(">>>>>>>>>>支付宝接收后台通知:{},SDK验签失败");
            params.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_201);
        }
        log.info(">>>>>>>>>>支付宝接收后台通知结束<<<<<<<<<<");
        log.info(">>>>>>>>>>参数："+params.toString());
        return params;
    }

    @Override
    public BaseResponse<JSONObject> asyncService(Map<String, String> verifySignature) {

        String paymentId = verifySignature.get("paymentId"); // 获取后台通知的数据，其他字段也可用类似方式获取
        String trade_no = verifySignature.get("trade_no");

        // 根据记录 手动补偿 使用支付id调用第三方支付接口查询，支付完成或者退款的,记录日志
        boolean result = examinePaymentTransaction(paymentId, PayConstant.PAY_STATUS_SUCCESS, trade_no,verifySignature,PayChannelConstant.ALI_PAY);
        if (!result){
            return setResultError("已经完成交易");
        }
        // 3.调用积分服务接口增加积分(处理幂等性问题) MQ
        //addMQIntegral(paymentTransaction); // 使用MQ
        //int i = 1 / 0; // 支付状态还是为待支付状态但是 积分缺增加
        return setResultSuccess();

    }


}
