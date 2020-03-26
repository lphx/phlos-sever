package cn.phlos.service.impl;

import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.PaymentChannelMapper;
import cn.phlos.mapper.entity.PaymentChannelEntity;
import cn.phlos.paycode.ask.factory.StrategyFactory;
import cn.phlos.paycode.ask.strategy.PayStrategy;
import cn.phlos.service.PayContextService;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.token.GenerateToken;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayContextServiceImpl extends BaseApiService<JSONObject> implements PayContextService {

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    
    @Autowired
    private PaymentTransacInfoService payMentTransacInfoService;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> toPayHtml(String channelId, String payToken) {

        // 1.使用渠道id获取渠道信息 classAddres
        PaymentChannelEntity paymentChannelEntity = paymentChannelMapper.selectBychannelId(channelId);
        if (paymentChannelEntity == null){
            setResultError("没有该支付渠道");
        }

        // 2.使用payToken获取支付参数
        BaseResponse<PaymentTransacDTO> paymentTransacDTOBaseResponse = payMentTransacInfoService.tokenByPayMentTransac(payToken);
        if (!isSuccess(paymentTransacDTOBaseResponse)){
            setResultError(paymentTransacDTOBaseResponse.getMsg());
        }
        //删除token
        generateToken.removeToken(payToken);
        PaymentTransacDTO paymentTransacDTO = paymentTransacDTOBaseResponse.getData();

        // 3.执行具体的支付渠道的算法获取html表单数据 策略设计模式 使用java反射机制 执行具体方法
        String classAddres = paymentChannelEntity.getClassAddres();
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(classAddres);
        String payHtml = payStrategy.toPayHtml(paymentChannelEntity, paymentTransacDTO);
        // 4.直接返回html
        JSONObject data = new JSONObject();
        data.put("payHtml", payHtml);
        return setResultSuccess(data);
    }

    @Override
    public BaseResponse<JSONObject> refund(String paymentId) {

        //1.根据paymentId查找出要退款的信息--前提是以支付的订单
        BaseResponse<PaymentTransacDTO> paymentTransacDTOBaseResponse = payMentTransacInfoService.refundByPayMent(paymentId);
        if (!isSuccess(paymentTransacDTOBaseResponse)){
            return setResultError(paymentTransacDTOBaseResponse.getMsg());
        }
        //2.获取到支付的信息和支付渠道的信息
        PaymentTransacDTO paymentTransacDTO = paymentTransacDTOBaseResponse.getData();
        PaymentChannelEntity paymentChannelEntity = paymentChannelMapper.selectBychannelId(paymentTransacDTO.getPaymentChannel());
        //3.执行具体的支付退款渠道进行退款
        String classAddres = paymentChannelEntity.getClassAddres();
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(classAddres);
        BaseResponse<JSONObject> refund = payStrategy.refund(paymentChannelEntity, paymentTransacDTO);
        // 4.直接返回data
        return refund;
    }
}
