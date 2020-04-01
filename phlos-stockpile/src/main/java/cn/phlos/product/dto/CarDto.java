package cn.phlos.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDto {

    private Long productId;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer qty;

}
