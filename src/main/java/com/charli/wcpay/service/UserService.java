package com.charli.wcpay.service;

import com.charli.wcpay.domain.User;

/**
 * 用户业务接口类
 */
public interface UserService {

    User saveWeChatUser(String code);
}
