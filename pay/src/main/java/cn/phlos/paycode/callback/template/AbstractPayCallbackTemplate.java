package cn.phlos.paycode.callback.template;

import cn.phlos.constant.PayConstant;
import cn.phlos.mapper.PaymentTransactionLogMapper;
import cn.phlos.mapper.entity.PaymentTransactionLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 获取所有请求的参数，封装成Map集合 并且验证是否被篡改
     *
     * @param req
     * @param resp
     * @return
     */
    public abstract Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp);

    /**
     * 异步回调执行业务逻辑
     *
     * @param verifySignature
     */
    @Transactional
    public abstract String asyncService(Map<String, String> verifySignature);

//    @Transactional
//    public abstract String asyncCallbackService(Map<String, String> verifySignature);

    public abstract String failResult();

    public abstract String successResult();

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
    public String asyncCallBack(HttpServletRequest req, HttpServletResponse resp) {
        // 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
        Map<String, String> verifySignatureMap = verifySignature(req, resp);
        // 2.将日志根据支付id存放到数据库中
        String paymentId = verifySignatureMap.get("paymentId");
        if (StringUtils.isEmpty(paymentId)) {
            return failResult();
        }

        // 3.采用异步形式写入日志到数据库中
        log.info(">>>>>>>>开始记录交易日志信息");
        threadPoolTaskExecutor.execute(new PayLogThread(paymentId,verifySignatureMap));
        // 4.201报文验证签名失败
        String result = verifySignatureMap.get(PayConstant.RESULT_NAME);
        // 4.201报文验证签名失败
        if (result.equals(PayConstant.RESULT_PAYCODE_201)) {
            return failResult();
        }
        // 5.执行的异步回调业务逻辑
        return asyncService(verifySignatureMap);
    }



    /**
     * 退款回调接口
     *
     * *1. 将报文数据存放到es <br>
     * 1. 验证报文参数<br>
     * 2. 将日志根据支付id存放到数据库中<br>
     * 3. 执行的异步回调业务逻辑<br>
     *
     */
   /* @Transactional
    public String refundCallBack(HttpServletRequest req, HttpServletResponse resp) {
        // 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
        Map<String, String> verifySignatureMap = verifySignature(req, resp);
        // 2.将日志根据支付id存放到数据库中
        String paymentId = verifySignatureMap.get("paymentId");
        if (StringUtils.isEmpty(paymentId)) {
            return failResult();
        }

        // 3.采用异步形式写入日志到数据库中
        log.info(">>>>>>>>开始记录交易日志信息");
        threadPoolTaskExecutor.execute(new PayLogThread(paymentId,verifySignatureMap));

        // 5.执行的异步回调业务逻辑
        return asyncService(verifySignatureMap);

    }*/

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
    }

}
