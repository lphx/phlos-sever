package cn.phlos.mapper;

import java.util.List;

import cn.phlos.mapper.entity.PaymentTransactionEntity;
import org.apache.ibatis.annotations.*;


@Mapper
public interface PaymentTransactionMapper {

	/**
	 * 保存支付的信息
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO `payment_transaction` VALUES (null, #{payAmount}, '0', #{userId}, #{orderId}, null, null, now(), null, null,null,#{paymentId},null,null);")
	public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO `payment_transaction` VALUES (#{id}, #{payAmount}, #{paymentStatus}, #{userId}, #{orderId}, #{revision}, #{createdBy}, #{createdTime}, #{updatedBy}, #{updatedTime},#{partyPayId},#{paymentId},#{paymentChannel},#{tradeNo});")
	public int savePaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo FROM payment_transaction WHERE ID=#{id};")
	public PaymentTransactionEntity selectById(Long id);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo  FROM payment_transaction WHERE payment_id=#{paymentId};")
	public PaymentTransactionEntity selectByPaymentId(String paymentId);

	@Update("update payment_transaction SET payment_status=#{paymentStatus},trade_no=#{tradeNo},payment_channel=#{paymentChannel} WHERE payment_id=#{paymentId}; ")
	public int updatePaymentStatus(@Param("paymentStatus") Integer paymentStatus, @Param("paymentId") String paymentId, @Param("tradeNo") String tradeNo ,@Param("paymentChannel")String paymentChannel);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo  FROM payment_transaction WHERE order_id=#{orderId} AND payment_status=0;")
	public PaymentTransactionEntity selectByOrderId(Long orderId);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo  FROM payment_transaction WHERE order_id=#{orderId} AND payment_status=1 limit 0,1;")
	public PaymentTransactionEntity selectByOrderIdAndPayment(Long orderId);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo  FROM payment_transaction WHERE payment_id=#{paymentId} and paymentStatus=0 ;")
	public PaymentTransactionEntity selectByPaymentNoPayment(String paymentId);

	@Select("SELECT ID AS ID ,pay_amount AS payAmount,payment_status AS paymentStatus,user_id AS userId, order_id AS orderId , created_time as createdTime ,partypay_id as partyPayId , payment_id as paymentId ,payment_channel as paymentChannel ,trade_no as tradeNo  FROM payment_transaction WHERE paymentStatus=0 ;")
	public List<PaymentTransactionEntity> selectByStatusStay();

}
