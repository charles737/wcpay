package com.charli.wcpay.service.impl;

import com.charli.wcpay.config.WeChatConfig;
import com.charli.wcpay.domain.User;
import com.charli.wcpay.domain.Video;
import com.charli.wcpay.domain.VideoOrder;
import com.charli.wcpay.dto.VideoOrderDto;
import com.charli.wcpay.mapper.UserMapper;
import com.charli.wcpay.mapper.VideoMapper;
import com.charli.wcpay.mapper.VideoOrderMapper;
import com.charli.wcpay.service.VideoOrderService;
import com.charli.wcpay.utils.CommonUtils;
import com.charli.wcpay.utils.HttpUtils;
import com.charli.wcpay.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @Override
    // 在添加事务的情况下，当生成的订单统一下单有误时，事务会回滚，生成的订单会回滚
    @Transactional(propagation = Propagation.REQUIRED) // propagation: 传播属性
    public String save(VideoOrderDto videoOrderDto) throws Exception {

        // 查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());

        // 查找用户信息
        User user = userMapper.findById(videoOrderDto.getUserId());

        // 生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());

        videoOrder.setUserId(user.getId());
        videoOrder.setNickname(user.getName());
        videoOrder.setHeadImg(user.getHeadImg());

        videoOrder.setState(0);
        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());

        videoOrderMapper.insert(videoOrder);

        // 获取codeUrl
        String codeUrl = unifiedOrder(videoOrder);
        return codeUrl;
    }

    @Override
    public VideoOrder findByOutTradeNo(String out_trade_no) {
        return videoOrderMapper.findByOutTradeNo(out_trade_no);
    }

    @Override
    public int updateVideoOrderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOrderByOutTradeNo(videoOrder);
    }

    /**
     * 统一下单方法
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        // 生成签名
        int i = 1/0; // 模拟事务
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", videoOrder.getVideoTitle());
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        params.put("trade_type", "NATIVE");

        // sign签名
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);

        // map转xml
        String payXml = WXPayUtil.mapToXml(params);

        System.out.println(payXml);

        // 统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(), payXml, 4000);

        if (null == orderStr){
            return null;
        }

        Map<String, String> unifiedOrderMap = WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());

        if (unifiedOrderMap != null){
            return unifiedOrderMap.get("code_url");
        }
        return null;
    }
}
