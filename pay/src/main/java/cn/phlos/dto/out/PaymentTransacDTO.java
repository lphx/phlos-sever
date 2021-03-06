package cn.phlos.dto.out;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "交易记录信息实体类")
public class PaymentTransacDTO {

	/** 主键ID */
	private Integer id;
	/** 支付金额 */
	private Long payAmount;
	/** 支付状态;0待支付1支付完成 2支付超时3支付失败 */
	private Integer paymentStatus;
	/** 用户ID */
	private Long userId;
	/** 订单号码 */
	private Long orderId;

	/** 创建时间 */
	private Date createdTime;

	/**
	 * 第三方支付id 支付宝、银联等
	 */
	private String paymentChannel;

	/**
	 * 退款凭证
	 */
	private String tradeNo;

	/**
	 * 使用雪花算法生产 支付系统 支付id
	 */
	private String paymentId;

	/**
	 * 支付宝退款的id
	 */
	private String refundId;

}
