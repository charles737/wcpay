package com.charli.wcpay.utils;

import com.charli.wcpay.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {

    // 签发者
    public static final String SUBJECT = "charli";

    // 过期时间：毫秒，一周
    public static final long EXPIRE = 1000*60*60*24*7;

    // 密钥
    public static final String APPSECRET = "charli256";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user){

        if (user.getId() == null || user.getName() == null || user.getHeadImg() == null){

            return null;
        }

        String token = Jwts.builder().setSubject(SUBJECT) // jwt所面向的用户，放登陆的用户名
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("img", user.getHeadImg())
                .setIssuedAt(new Date()) // jwt的签发时间
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE)) // jwt的过期时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){

        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET)
                    .parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){}
        return null;


    }
}
