package cn.phlos.mapper;

import cn.phlos.mapper.entity.PaymentChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentChannelMapper {

    /**
     * 查找所有通道开启（状态为0）的支付渠道
     * @return
     */
    @Select("SELECT channel_Name  AS channelName , channel_Id AS channelId, merchant_Id AS merchantId,sync_Url AS syncUrl, asyn_Url AS asynUrl,public_Key AS publicKey, private_Key AS privateKey,channel_State AS channelState ,class_ADDRES as classAddres ,retry_beanid as retryBeanId     FROM payment_channel WHERE CHANNEL_STATE='0';")
    public List<PaymentChannelEntity> selectAll();

    /**
     * 根据渠道ID去查询对应的信息
     * @param channelId
     * @return
     */
    @Select("SELECT channel_Name  AS channelName , channel_Id AS channelId, merchant_Id AS merchantId,sync_Url AS syncUrl, asyn_Url AS asynUrl,public_Key AS publicKey, private_Key AS privateKey,channel_State AS channelState ,class_ADDRES as classAddres   ,retry_beanid as retryBeanId    FROM payment_channel WHERE CHANNEL_STATE='0'  AND channel_Id=#{channelId} ;")
    PaymentChannelEntity selectBychannelId(String channelId);

}
