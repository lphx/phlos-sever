package cn.phlos.paycode.log;

import cn.phlos.constant.PayConstant;
import cn.phlos.mapper.PaymentTransactionLogMapper;
import cn.phlos.mapper.PaymentTransactionMapper;
import cn.phlos.mapper.entity.PaymentTransactionEntity;
import cn.phlos.mapper.entity.PaymentTransactionLogEntity;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.core.utils.SpringContextUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public  class AbstractPayment extends BaseApiService<JSONObject> {


    @Autowired
    PaymentTransactionMapper paymentTransactionMapper;
    @Autowired
    PaymentTransactionLogMapper paymentTransactionLogMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;



   /* public BaseResponse<JSONObject> failResult(String msg) {
        return setResultError(msg);
    }
    public BaseResponse<JSONObject> successResult(String msg) {
        return setResultSuccess(msg);
    }*/

    /**
     * 检查交易信息，手动补偿，交易状态修改
     */
    public boolean examinePaymentTransaction(String paymentId,Integer paymentStatus,String  voucher,Object verifySignatureMap,String paymentChannel){
        log.info("========开始交易操作"+"paymentId:"+paymentId+"payStatus:"+paymentStatus+"voucher:"+voucher);
        // 根据记录 手动补偿 使用支付id调用第三方支付接口查询，支付完成或者退款的
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(paymentId);
        if (paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)||paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_DELETE)) {
            // 网络重试中，之前已经支付过
            log.info(">>>>>>>已经交易成果或者退款成功");
            return false;
        }
        paymentTransactionMapper.updatePaymentStatus( paymentStatus, paymentId, voucher,paymentChannel);

        // 4.采用异步形式写入日志到数据库中
        log.info(">>>>>>>>开始记录交易日志信息");
        threadPoolTaskExecutor.execute(new PayLogThread(paymentId,verifySignatureMap));
        log.info("========结束交易操作============");

        return true;
    }


    /**
     * 交易成功--基于MQ增加积分
     */
    @Async
    public void addMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        //integralProducer.send(jsonObject);
    }

    /**
     * 交易退款--基于MQ减少积分
     */
    @Async
    public void deleteMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        //integralProducer.send(jsonObject);
    }

    public void payLog(String paymentId, Object verifySignature){
        PaymentTransactionLogEntity paymentTransactionLogEntity = new PaymentTransactionLogEntity();
        paymentTransactionLogEntity.setTransactionId(paymentId);
        paymentTransactionLogEntity.setAsyncLog(verifySignature.toString());
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLogEntity);
        log.info(">>>>>>>>交易日志信息记录成功：{}"+paymentTransactionLogEntity);
    }

    @Data
    @AllArgsConstructor//生成全参数构造函数
    protected class PayLogThread implements Runnable{

        private String paymentId;
        private Object verifySignature;


        @Override
        public void run() {
            payLog(paymentId,verifySignature);
        }
    }

}
