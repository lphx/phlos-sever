package cn.phlos.constant;

/**
 * 支付相关常量数据
 *
 */
public interface PayConstant {

    String RESULT_NAME = "result";
    String RESULT_PAYCODE_201 = "201";
    String RESULT_PAYCODE_200 = "200";
    /**
     * 已经支付成功状态
     */
    Integer PAY_STATUS_SUCCESS = 1;
    /**
     * 已经退货成功状态
     */
    Integer PAY_STATUS_DELETE = 5;

    /**
     * 返回交易通知成功
     */
    String RESULT_SUCCESS = "200";
    /**
     * 返回交易失败通知
     */
    String RESULT_FAIL = "500";
}
