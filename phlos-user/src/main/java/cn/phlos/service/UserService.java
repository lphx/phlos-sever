package cn.phlos.service;

import cn.phlos.dto.UserInputDto;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户服务层
 * @Author: Penghong Li
 * @Date: Create in 9:57 2020/4/2
 */

public interface UserService {
    public BaseResponse<JSONObject> register(UserInputDto userInputDto, String registerCode);
}
