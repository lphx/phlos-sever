package cn.phlos.mapper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Data
public class PaymentTransactionEntity {
	/** 主键ID */
	//@Id
	private Long id;
	/** 支付金额 */
	private Long payAmount;
	/** 支付状态;0待支付1已经支付2支付超时3支付失败 */
	private Integer paymentStatus;
	/** 用户ID */
	private Long userId;
	/** 订单号码 */
	private Long orderId;
	/** 乐观锁 */
	private Integer revision;
	/** 创建人 */
	private String createdBy;
	/** 创建时间 */
	private Date createdTime;
	/** 更新人 */
	private String updatedBy;
	/** 更新时间 */
	private Date updatedTime;

	/**
	 * 第三方支付id 支付宝、银联等 在第三方支付渠道完成后分配一个支付id 对账使用
	 */
	private String partyPayId;

	/**
	 * 使用雪花算法生产 支付系统 支付id
	 */
	private String paymentId;

	/**
	 * 退款请求号：退款时专用字段
	 */
	private String tradeNo;
	/**
	 * 支付渠道
	 */
	private String paymentChannel;

	// 对账主动查询第三方支付系统区别：银联支付使用paymentId去调用银联支付接口查询 支付宝在异步异步回调接口时，返回partyPayId
	// 使用第三方支付partyPayIdid查询


}