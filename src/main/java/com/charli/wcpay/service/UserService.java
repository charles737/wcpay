package com.charli.wcpay.service;

import com.charli.wcpay.domain.JsonData;
import com.charli.wcpay.domain.User;

import java.util.Map;

/**
 * 用户业务接口类
 */
public interface UserService {

    User saveWeChatUser(String code);

    /**
     * 通过手机号注册
     * @param userMap
     * @return
     */
    int saveByPhone(Map<String, String> userInfo);
}
