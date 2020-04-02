package cn.phlos.api;

import cn.phlos.dto.UserInputDto;
import cn.phlos.dto.UserLoginInpDTO;
import cn.phlos.dto.UserOutputDto;
import cn.phlos.service.UserService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户的api接口层
 *
 * @Author: Penghong Li
 * @Date: Create in 9:34 2020/4/2
 */
@Api(tags = "用户api接口层")
@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param userInpDTO
     * @param registerCode
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "会员用户注册信息接口")
    BaseResponse<JSONObject> register(@RequestBody UserInputDto userInpDTO,
                                      @RequestParam("registerCode") String registerCode){
        return userService.register(userInpDTO,registerCode);
    }


    /**
     * 用户登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO){
        return userService.login(userLoginInpDTO);
    }

    /**
     * 根据手机号码查询是否已经存在,如果存在返回当前用户信息
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "根据手机号码查询是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "用户手机号码"), })
    @PostMapping("/existMobile")
    BaseResponse<UserOutputDto> existMobile(@RequestParam("mobile") String mobile){
        return userService.existMobile(mobile);
    }

    /**
     * 根据token查询用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "/getUserInfo")
    BaseResponse<UserOutputDto> getInfo(@RequestParam("token") String token){
        return userService.getInfo(token);
    }

}
