package cn.phlos.order.mapper;

import cn.phlos.order.mapper.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface OrderMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert order values(null,#{userId},#{transactionId},#{amount},#{orderItem},#{createBy},now(),null.null)")
    public int saveOrder(OrderEntity orderEntity);

}
