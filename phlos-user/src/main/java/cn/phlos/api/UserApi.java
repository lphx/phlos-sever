package cn.phlos.api;

import cn.phlos.dto.UserInputDto;
import cn.phlos.service.UserService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户的api接口层
 *
 * @Author: Penghong Li
 * @Date: Create in 9:34 2020/4/2
 */
@Api(tags = "用户api接口层")
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

}
