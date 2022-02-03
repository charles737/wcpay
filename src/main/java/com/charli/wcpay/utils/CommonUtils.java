package com.charli.wcpay.utils;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * 常用工具类的封装,md5、uuid等
 */
public class CommonUtils {

    /**
     * 生成uuid，用来标识一笔订单，也用作nonce_str
     * @return
     */
    public static String generateUUID(){

        String uuid = UUID.randomUUID().toString()
                .replaceAll("-", "").substring(0, 32);

        return uuid;
    }

    /**
     * MD5常用工具类
     * @param data
     * @return
     */
    public static String MD5(String data){

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] array = md5.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 头像
     */
    private static final String[] headImg = {
        "https://github.com/charles737/Img/raw/main/head_image/IMG_7279.jpg",
            "https://github.com/charles737/Img/raw/main/head_image/IMG_7771.jpg"
    };

    /**
     * 获取随机头像
     * @return
     */
    public static String getHeadImg() {
        int size = headImg.length;
        Random random = new Random();
        int index = random.nextInt(size);
        return headImg[index];
    }

}
