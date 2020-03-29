package cn.phlos.paycode.callback.template;

import cn.phlos.constant.PayConstant;
import cn.phlos.mapper.PaymentTransactionLogMapper;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.mapper.entity.PaymentTransactionLogEntity;
import cn.phlos.paycode.log.AbstractPayment;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 使用模版方法重构异步回调代码
 */
@Slf4j
@Component
public abstract class AbstractPayCallbackTemplate extends AbstractPayment {


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 获取所有请求的参数，封装成Map集合 并且验证是否被篡改
     *
     * @param req
     * @param resp
     * @return
     */
    public abstract Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) throws AlipayApiException;

    /**
     * 异步回调执行业务逻辑
     *
     * @param verifySignature
     */
    @Transactional
    public abstract BaseResponse<JSONObject> asyncService(Map<String, String> verifySignature);

//    @Transactional
//    public abstract String asyncCallbackService(Map<String, String> verifySignature);




//    private String payParameter;
//    public void getPayParameter(String templateName){
//        this.payParameter =  PayRequest.getEnum(templateName);
//    }
    /**
     * 支付回调接口
     *
     * *1. 将报文数据存放到es <br>
     * 1. 验证报文参数<br>
     * 2. 将日志根据支付id存放到数据库中<br>
     * 3. 执行的异步回调业务逻辑<br>
     *
     */
    @Transactional
    public BaseResponse<JSONObject> asyncCallBack(HttpServletRequest req, HttpServletResponse resp) throws AlipayApiException {
        // 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
        Map<String, String> verifySignatureMap = verifySignature(req, resp);
        // 2.201报文验证签名失败
        String result = verifySignatureMap.get(PayConstant.RESULT_NAME);
        if (result.equals(PayConstant.RESULT_PAYCODE_201)) {
            return setResultError("报文验证签名失败");
        }
        // 3.将日志根据支付id存放到数据库中
        String paymentId = verifySignatureMap.get("paymentId");
        if (StringUtils.isEmpty(paymentId)) {
            return setResultError("无此交易信息");
        }



        // 5.执行的异步回调业务逻辑
        return asyncService(verifySignatureMap);
    }


    /**
     * 检查交易信息，手动补偿，交易状态修改
     *//*
    public boolean examinePaymentTransaction(String paymentId,String payStatus,String  voucher,String paymentChannel){
        log.info("========开始交易操作"+"paymentId:"+paymentId+"payStatus:"+payStatus+"voucher:"+voucher);
        // 根据记录 手动补偿 使用支付id调用第三方支付接口查询，支付完成或者退款的
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(paymentId);
        if (paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)||paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_DELETE)) {
            // 网络重试中，之前已经支付过
            log.info(">>>>>>>已经交易成果或者退款成功");
            return false;
        }
        // 2.将状态改为已经支付或者退款成功
        Integer paymentStatus = PayConstant.PAY_STATUS_SUCCESS;
        //如果是包含退款，就更新状态为5表示退款的订单
        if(!StringUtils.isEmpty(payStatus) && payStatus.equals("refund")){
            paymentStatus = PayConstant.PAY_STATUS_DELETE;
        }
        paymentTransactionMapper.updatePaymentStatus( paymentStatus+ "", paymentId, paymentChannel, voucher);
        log.info("========结束交易操作============");

        return true;
    }


    *//**
     * 交易成功--基于MQ增加积分
     *//*
    @Async
    public void addMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        //integralProducer.send(jsonObject);
    }

    *//**
     * 交易退款--基于MQ减少积分
     *//*
    @Async
    public void deleteMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        //integralProducer.send(jsonObject);
    }

    private void payLog(String paymentId,Map<String, String> verifySignature){
        PaymentTransactionLogEntity paymentTransactionLogEntity = new PaymentTransactionLogEntity();
        paymentTransactionLogEntity.setTransactionId(paymentId);
        paymentTransactionLogEntity.setAsyncLog(verifySignature.toString());
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLogEntity);
        log.info(">>>>>>>>交易日志信息记录成功：{}"+paymentTransactionLogEntity);
    }

    @Data
    @AllArgsConstructor //生成全参数构造函数
    class PayLogThread implements Runnable{

        private String paymentId;
        private Map<String, String> verifySignature;


        @Override
        public void run() {
            payLog(paymentId,verifySignature);
        }
    }*/

}
