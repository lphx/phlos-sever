package cn.phlos.mapper;

import cn.phlos.mapper.entity.PaymentTransactionLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentTransactionLogMapper {

    @Insert("INSERT INTO `payment_transaction_log` VALUES (NULL, NULL, #{asyncLog},NULL, #{transactionId}, null, null, NOW(), null, NOW(),0);")
    public int insertTransactionLog(PaymentTransactionLogEntity paymentTransactionLog);

}
