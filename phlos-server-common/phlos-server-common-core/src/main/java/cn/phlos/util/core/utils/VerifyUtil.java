package cn.phlos.util.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 验证码工具类
 *
 * @Author: Penghong Li
 * @Date: Create in 11:47 2020/4/2
 */

@Component
public class VerifyUtil {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 存储验证码
     * @param mobile
     * @param verityCode
     */
    public  void addMobileVerify(String mobile ,String verityCode){
        //设置30分钟有效期
        redisUtil.setString(mobile,verityCode,30*60*1000l);
    }

    /**
     * 查找验证码
     * @param mobile
     * @return
     */
    public  String findMobileVerify(String mobile){
        //设置30分钟有效期
        return redisUtil.getString(mobile);
    }

}
