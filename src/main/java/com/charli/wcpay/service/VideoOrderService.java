package com.charli.wcpay.service;

import com.charli.wcpay.domain.VideoOrder;
import com.charli.wcpay.dto.VideoOrderDto;
import org.apache.ibatis.annotations.Param;

/**
 * 订单接口
 */
public interface VideoOrderService {

    /**
     * 下单接口
     * @param videoOrderDto
     * @return
     */
    String save(VideoOrderDto videoOrderDto) throws Exception;

    /**
     * 根据流水号查找订单
     * @param out_trade_no
     * @return
     */
    VideoOrder findByOutTradeNo(String out_trade_no);

    /**
     * 根据流水号更新订单
     * @param videoOrder
     * @return
     */
    int updateVideoOrderByOutTradeNo(VideoOrder videoOrder);
}
