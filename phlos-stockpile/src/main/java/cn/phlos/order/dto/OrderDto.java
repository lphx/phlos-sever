package cn.phlos.order.dto;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;
    private Long userId;
    private Long amount;
    private Long orderItem;
    private Integer state;

}
