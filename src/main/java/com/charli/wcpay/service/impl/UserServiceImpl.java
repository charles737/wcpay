package com.charli.wcpay.service.impl;

import com.charli.wcpay.config.WeChatConfig;
import com.charli.wcpay.domain.User;
import com.charli.wcpay.mapper.UserMapper;
import com.charli.wcpay.service.UserService;
import com.charli.wcpay.utils.CommonUtils;
import com.charli.wcpay.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWeChatUser(String code) {

        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),
                weChatConfig.getOpenAppid(), weChatConfig.getOpenAppsecret(), code);

        // 获取access_token
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);

        if (baseMap == null || baseMap.isEmpty()){
            return null;
        }

        String accessToken = (String)baseMap.get("access_token");
        String openId = (String)baseMap.get("openid");

        User dbUser = userMapper.findByOpenId(openId);
        if (dbUser != null){

            // 更新用户，直接返回
            return dbUser;
        }

        // 获取用户基本信息
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(), accessToken, openId);

        // 获取access_token
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoUrl);

        if (baseUserMap == null || baseUserMap.isEmpty()){
            return null;
        }

        String nickname = (String)baseUserMap.get("nickname");

        // sex从Double类型转成int类型
        Double sexTemp = (Double) baseUserMap.get("sex");
        int sex = sexTemp.intValue();
        String province = (String)baseUserMap.get("province");
        String city = (String)baseUserMap.get("city");
        String country = (String)baseUserMap.get("country");
        // 中国||浙江||杭州
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String headimgurl = (String)baseUserMap.get("headimgurl");
        String finalAddress = sb.toString();
        try {
            // 解决乱码
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        // 封装user对象
        User user = new User();
        user.setName(nickname);
        user.setCity(finalAddress);
        user.setHeadImg(headimgurl);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());
        userMapper.save(user);

        return user;
    }

    @Override
    public int saveByPhone(Map<String, String> userInfo) {
        User user = parseToUser(userInfo);
        return userMapper.saveByPhone(user);
    }

    /**
     * Map to User
     * @param userInfo
     * @return
     */
    private User parseToUser(Map<String, String> userInfo) {

        User user = new User();
        user.setName(userInfo.get("name"));
        user.setPhone(userInfo.get("phone"));
        user.setHeadImg(CommonUtils.getHeadImg());
        Date utilDate = new Date();
        // 下面注释部分目前不使用。将配置中的GMT改为GMT%2B8，时间慢8小时的问题解决
        // 将java.util.Date转为java.sql.Date存储到数据库中
//        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        // 使用java.sql.Timestamp这个类来进行转换
//        Timestamp t = new Timestamp(utilDate.getTime());
        user.setCreateTime(utilDate);

        return user;
    }
}
