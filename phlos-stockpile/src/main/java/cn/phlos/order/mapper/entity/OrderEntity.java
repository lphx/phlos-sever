package cn.phlos.order.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderEntity {

    private Long id;
    private Long userId;
    private Long transactionId;
    private Long amount;
    private Integer state;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;



}
