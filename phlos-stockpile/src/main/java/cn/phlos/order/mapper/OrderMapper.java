package cn.phlos.order.mapper;

import cn.phlos.order.mapper.entity.OrderEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface OrderMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into `order` values(null,#{userId},#{transactionId},#{amount},#{state},#{createBy},now(),null,null)")
    public int saveOrder(OrderEntity orderEntity);

    /**
     * 更新订单的交易状态
     * @param transactionId
     * @param state
     * @param updateBy
     * @param orderId
     * @return
     */
    @Update("update `order` set transaction_id=#{transactionId} ,state=#{state} ,update_by=#{updateBy} ,update_time=#{updateTime} where id=#{orderId};")
    public int updateOrderPaymentState(@Param("transactionId")Long transactionId, @Param("state")Integer state, @Param("updateBy")String updateBy, @Param("orderId")Long orderId, @Param("updateTime")Date updateTime);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    @Select("SELECT id as id ,user_id as userId ,transaction_id as transactionId ,amount as amount ,state as state ,create_by as createBy ,create_time as createTime ,update_by as updateBy ,update_time as update_time FROM `order` WHERE id=#{orderId}  limit 0,1")
    public OrderEntity findOrder(@Param("orderId")Long orderId);
}
