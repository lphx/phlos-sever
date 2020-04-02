package cn.phlos.service.impl;

import cn.phlos.dto.UserInputDto;
import cn.phlos.mapper.UserMapper;
import cn.phlos.mapper.entity.UserDo;
import cn.phlos.service.UserService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.core.bean.EntityUtils;
import cn.phlos.util.core.utils.AESUtil;
import cn.phlos.util.core.utils.MD5Util;
import cn.phlos.util.core.utils.RegexUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现层
 * @Author: Penghong Li
 * @Date: Create in 9:57 2020/4/2
 */

@Service
public class UserServiceImpl extends BaseApiService<JSONObject> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseResponse<JSONObject> register(UserInputDto userInputDto, String registerCode) {
        //后台再次判断User传来的参数是否为空
        String userName = userInputDto.getUserName();
        if (StringUtils.isEmpty(userName)){
            return setResultError("用户名不能为空");
        }

        String mobile = userInputDto.getMobile();
        if (StringUtils.isEmpty(mobile)|| RegexUtils.checkMobile(mobile)){
            return setResultError("手机号码格式错误");
        }

        String password = userInputDto.getPassword();
        if (StringUtils.isEmpty(password)){
            return setResultError("密码不能为空");
        }

        if (StringUtils.isEmpty(registerCode)){
            return setResultError("验证码不能为空");
        }

        //2.通过手机号获取验证码


        //3.使用AES对称加密算法
        String newPassword = AESUtil.bcAESEncryption(password);
        userInputDto.setPassword(newPassword);

        // 4.调用数据库插入数据 将请求的dto参数转换DO
        UserDo userDo = EntityUtils.dtoToDo(userInputDto, UserDo.class);
        return userMapper.register(userDo) > 0 ? setResultSuccess("注册成功") : setResultError("注册失败!");
    }
}
