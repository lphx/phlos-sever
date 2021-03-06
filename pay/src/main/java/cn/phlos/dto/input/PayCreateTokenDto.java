package cn.phlos.dto.input;



import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@ApiModel(value = "交易信息实体类")
public class PayCreateTokenDto {

    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    private Long payAmount;
    /**
     * 订单号码
     */
    @NotNull(message = "订单号码不能为空")
    private Long orderId;

    /**
     * userId
     */
    @NotNull(message = "userId不能空")
    private Long userId;

}
