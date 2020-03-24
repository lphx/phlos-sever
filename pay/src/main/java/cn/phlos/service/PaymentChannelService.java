package cn.phlos.service;

import cn.phlos.dto.out.PaymentChannelDTO;

import java.util.List;

public interface PaymentChannelService {
    public List<PaymentChannelDTO> selectAll();
}
