package com.charli.wcpay.mapper;

import com.charli.wcpay.domain.VideoOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoOrderMapperTest {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Test
    public void insert() {

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setDel(0);
        videoOrder.setHeadImg("www.google.com");
        videoOrder.setNickname("hexo");
        videoOrder.setTotalFee(22222);
        videoOrder.setVideoTitle("Java中级架构师实战课程");
        videoOrderMapper.insert(videoOrder);
        assertNotNull(videoOrder.getId());
    }

    @Test
    public void findById() {

        VideoOrder videoOrder = videoOrderMapper.findById(1);
        assertNotNull(videoOrder);
    }

    @Test
    public void findByOutTradeNo() {
    }

    @Test
    public void del() {
    }

    @Test
    public void findMyOrderList() {
    }

    @Test
    public void updateVideoOrderByOutTradeNo() {
    }
}