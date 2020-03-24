package cn.phlos.service.impl;

import cn.phlos.dto.out.PaymentChannelDTO;
import cn.phlos.mapper.PaymentChannelMapper;
import cn.phlos.mapper.entity.PaymentChannelEntity;
import cn.phlos.service.PaymentChannelService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.core.bean.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentChannelServiceImpl extends BaseApiService< List<PaymentChannelEntity>> implements PaymentChannelService {
    
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    
    @Override
    public List<PaymentChannelDTO> selectAll() {
        List<PaymentChannelEntity> paymentChanneList = paymentChannelMapper.selectAll();
        return MapperUtils.mapAsList(paymentChanneList, PaymentChannelDTO.class);
    }
}
