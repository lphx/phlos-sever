package cn.phlos.mapper;

import java.util.List;

import cn.phlos.mapper.entity.PaymentTransactionEntity;
import org.apache.ibatis.annotations.*;


@Mapper
public interface PaymentTransactionMapper {
	@Options(useGeneratedKeys = true, keyProperty = "id")
	/**
	 * 保存支付的信息
	 */
	@Insert("INSERT INTO `payment_transaction` VALUES (null, #{payAmount}, '0', #{userId}, #{orderId}, null, null, now(), null, null,null,#{paymentId},null,null,0);")
	public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

	@Insert("INSERT INTO `payment_transaction` VALUES (#{id}, #{payAmount}, #{paymentStatus}, #{userId}, #{orderId}, #{revision}, #{createdBy}, #{createdTime}, #{updatedBy}, #{updatedTime},#{partyPayId},#{paymentId},#{paymentChannel},#{tradeNo},#{refundId});")
	public int savePaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo ,refund_id as refundId FROM payment_transaction WHERE ID=#{id};")
	public PaymentTransactionEntity selectById(Long id);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo ,refund_id as refundId FROM payment_transaction WHERE PAYMENT_ID=#{paymentId};")
	public PaymentTransactionEntity selectByPaymentId(String paymentId);

	@Update("update payment_transaction SET PAYMENT_STATUS=#{paymentStatus},payment_channel=#{paymentChannel},trade_no=#{tradeNo}   WHERE PAYMENT_ID=#{paymentId}; ")
	public int updatePaymentStatus(@Param("paymentStatus") String paymentStatus, @Param("paymentId") String paymentId,
                                   @Param("paymentChannel") String paymentChannel, @Param("tradeNo") String tradeNo);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo ,refund_id as refundId FROM payment_transaction WHERE PAYMENT_ID=#{paymentId} and paymentStatus=0 ;")
	public PaymentTransactionEntity selectByPaymentNoPayment(String paymentId);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo ,refund_id as refundId FROM payment_transaction WHERE paymentStatus=0 ;")
	public List<PaymentTransactionEntity> selectByStatusStay();

}
