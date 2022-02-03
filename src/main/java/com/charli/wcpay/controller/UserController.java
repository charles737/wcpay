package com.charli.wcpay.controller;


import com.charli.wcpay.domain.JsonData;
import com.charli.wcpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registry")
    public JsonData registry(@RequestBody Map<String, String> userInfo) {

        if (userInfo == null) {
            return JsonData.buildError("请重新输入!");
        }
        return userService.saveByPhone(userInfo)==1 ? JsonData.buildSuccess() : JsonData.buildError("注册失败!", -1);
    }
}
