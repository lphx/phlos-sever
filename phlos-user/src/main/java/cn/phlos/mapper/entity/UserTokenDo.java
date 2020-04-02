package cn.phlos.mapper.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: Penghong Li
 * @Date: Create in 22:37 2020/4/2
 */


@Data
public class UserTokenDo{
    /**
     * id
     */
    private Long id;
    /**
     * 用户token
     */
    private String token;
    /**
     * 登陆类型
     */
    private String loginType;

    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 用户userId
     */
    private Long userId;

    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 修改时间
     *
     */
    private Date updateTime;

    /**
     * 是否可用 0可用 1不可用
     */
    private Long isAvailability;

}
