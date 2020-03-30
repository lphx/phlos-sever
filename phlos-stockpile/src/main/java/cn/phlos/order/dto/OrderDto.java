package cn.phlos.order.dto;

import lombok.Data;

@Data
public class OrderDto {

    private Long orderId;
    private Long userId;
    private Long amount;
    private Long orderItem;

}
