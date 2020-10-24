package com.charli.wcpay.controller;

import com.charli.wcpay.config.WeChatConfig;
import com.charli.wcpay.domain.JsonData;
import com.charli.wcpay.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("test")
    public String test(){
        return "hello wcpay! - 737";
    }

    @Autowired
    private WeChatConfig weChatConfig;

    @RequestMapping("test_config")
    public JsonData testConfig(){
        System.out.println(weChatConfig.getAppId());
        return JsonData.buildSuccess(weChatConfig.getAppId());
    }

    @Autowired
    private VideoMapper videoMapper;

    @RequestMapping("test_db")
    public Object testDB(){
        return videoMapper.findAll();
    }

    /**
     * 查
     * @return
     */
    @RequestMapping("test_findbyid")
    public Object testFindById(){
        return videoMapper.findById(2);
    }
}
