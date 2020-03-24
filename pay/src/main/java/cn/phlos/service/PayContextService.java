package cn.phlos.service;

import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;

public interface PayContextService {

    public BaseResponse<JSONObject> toPayHtml( String channelId, String payToken);

}
