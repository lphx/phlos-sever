package cn.phlos.service.impl;

import cn.phlos.dto.UserInputDto;
import cn.phlos.dto.UserLoginInpDTO;
import cn.phlos.dto.UserOutputDto;
import cn.phlos.mapper.UserMapper;
import cn.phlos.mapper.UserTokenMapper;
import cn.phlos.mapper.entity.UserDo;
import cn.phlos.mapper.entity.UserTokenDo;
import cn.phlos.service.UserService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.constants.Constants;
import cn.phlos.util.core.bean.ConvertBeanUtils;
import cn.phlos.util.core.bean.EntityUtils;
import cn.phlos.util.core.transaction.RedisDataSoureceTransaction;
import cn.phlos.util.core.type.TypeCastHelper;
import cn.phlos.util.core.utils.AESUtil;
import cn.phlos.util.core.utils.MD5Util;
import cn.phlos.util.core.utils.RegexUtils;
import cn.phlos.util.core.utils.VerifyUtil;
import cn.phlos.util.token.GenerateToken;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现层
 * @Author: Penghong Li
 * @Date: Create in 9:57 2020/4/2
 */

@Service
@Slf4j
public class UserServiceImpl extends BaseApiService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;

    @Autowired
    private  VerifyUtil verifyUtil;

    @Override
    public BaseResponse<JSONObject> register(UserInputDto userInputDto, String registerCode) {
        log.info("用户注册：userInputDto：{} |registerCode:{}",userInputDto.toString(),registerCode);
        //后台再次判断User传来的参数是否为空
        String userName = userInputDto.getUserName();
        if (StringUtils.isEmpty(userName)){
            return setResultError("用户名不能为空");
        }

        String mobile = userInputDto.getMobile();
        if (StringUtils.isEmpty(mobile)|| !RegexUtils.checkMobile(mobile)){
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
        String code = verifyUtil.findMobileVerify(mobile);
        if (StringUtils.isEmpty(code)){
            return setResultError("验证码错误");
        }
        //删除验证码
       verifyUtil.delete(mobile);
        //3.使用A加密算法
        String newPassword = MD5Util.MD5(password);
        userInputDto.setPassword(newPassword);

        // 4.调用数据库插入数据 将请求的dto参数转换DO
        UserDo userDo = EntityUtils.dtoToDo(userInputDto, UserDo.class);
        return userMapper.register(userDo) > 0 ? setResultSuccess("注册成功") : setResultError("注册失败!");
    }

    @Override
    public BaseResponse<JSONObject> login(UserLoginInpDTO userLoginInpDTO) {
        log.info("用户注册：userLoginInpDTO：{}",userLoginInpDTO.toString());
        // 1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }

        // 2.对登陆密码实现解密
        String newPassWord = MD5Util.MD5(password);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        UserDo userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误!");
        }

        // 用户每一个端登陆成功之后，会对应生成一个token令牌（临时且唯一）存放在redis中作为rediskey value userid
        TransactionStatus transactionStatus = null;
        try {
        // 4.获取userid
        Long userId = userDo.getUserId();
        // 5.根据userId+loginType 查询当前登陆类型账号之前是否有登陆过，如果登陆过 清除之前redistoken
        UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
        transactionStatus = redisDataSoureceTransaction.begin();
        if(!StringUtils.isEmpty(userTokenDo)) {
            //表示已登录过了，要清除原本的token
            String token = userTokenDo.getToken();
            generateToken.removeToken(token);
            //修改数据库的记录，改为1
            int availability = userTokenMapper.updateTokenAvailability(token);
            if (!toDaoResult(availability)) {
                return setResultError("系统错误");
            }
        }
            // 如果有传递openid参数，修改到数据中
            String qqOpenId = userLoginInpDTO.getQqOpenId();
            if (!StringUtils.isEmpty(qqOpenId)) {
                userMapper.updateUserOpenId(qqOpenId, userId);
            }

            // .生成对应用户令牌存放在redis中

            //创建新的entity
            UserTokenDo userToken = new UserTokenDo();
            userToken.setUserId(userId);
            userToken.setLoginType(userLoginInpDTO.getLoginType());
            // 插入新的token
            String keyPrefix = Constants.USER_TOKEN_KEYPREFIX + loginType;
            String newToken = generateToken.createToken(keyPrefix, userId + "");
            userToken.setToken(newToken);
            //插入设备信息
            userToken.setDeviceInfo(deviceInfor);
            //保存数据
            int insertUserToken = userTokenMapper.insertUserToken(userToken);
            if (!toDaoResult(insertUserToken)){
                return setResultError("系统错误");
            }

            JSONObject data = new JSONObject();
            data.put("token",newToken);
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess(data);
        } catch (Exception e) {
            try {
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception e2) {
                    // TODO: handle exception
            }
            return setResultError("系统错误!");
        }


    }


    @Override
    public BaseResponse<UserOutputDto> existMobile(String mobile) {
        // 1.验证参数
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }

        // 2.根据手机号码查询用户信息 单独定义code 表示是用户信息不存在把
        UserDo userEntity = userMapper.existMobile(mobile);
        if (userEntity == null) {
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203, "用户信息不存在!");
        }

        // 3.将do转换成dto
        return setResultSuccess(ConvertBeanUtils.doToDto(userEntity, UserOutputDto.class));
    }

    @Override
    public BaseResponse<UserOutputDto> getInfo(String token) {
        // 1.验证token参数
        if (StringUtils.isEmpty(token)) {
            return setResultError("token不能为空!");
        }
        // 2.使用token查询redis 中的userId
        String redisUserId = generateToken.getToken(token);
        if (StringUtils.isEmpty(redisUserId)) {
            return setResultError("token已经失效或者token错误!");
        }
        // 3.使用userID查询 数据库用户信息
        Long userId = TypeCastHelper.toLong(redisUserId);
        UserDo userDo = userMapper.findByUserId(userId);
        if (userDo == null) {
            return setResultError("用户不存在!");
        }
        // 下节课将 转换代码放入在BaseApiService
        return setResultSuccess(ConvertBeanUtils.doToDto(userDo, UserOutputDto.class));
    }

    @Override
    public BaseResponse<UserOutputDto> ssoLogin(UserLoginInpDTO userLoginInpDTO) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> createCodeToMobile(String mobile) {
        String code = verifyUtil.createCode();
        verifyUtil.addMobileVerify(mobile,code);
        log.info("发送验证码：mobile：{} | code:{}",mobile,code);

        return setResultSuccess(code);
    }
}
