package com.charli.wcpay;

import com.charli.wcpay.domain.User;
import com.charli.wcpay.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

public class CommonTest {

    @Test
    public void testGeneJwt(){

        User user = new User();
        user.setId(007);
        user.setName("詹姆斯邦德");
        user.setHeadImg("www.007.com");

        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println(token);
    }

    @Test
    public void testCheck(){

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGFybGkiLCJpZCI6NywibmFtZSI6IuipueWnhuaWr-mCpuW-tyIsImltZyI6Ind3dy4wMDcuY29tIiwiaWF0IjoxNjAyMTMwMjM1LCJleHAiOjE2MDI3MzUwMzV9.Tj1pc06MQzVsPjKK_GDpTTxREVanjo3OrubGj3HZu7M";
        Claims claims = JwtUtils.checkJWT(token);
        if (claims != null){
            int id = (Integer) claims.get("id");
            String name = (String) claims.get("name");
            String img = (String) claims.get("img");
            System.out.println(id);
            System.out.println(name);
            System.out.println(img);
        }else {
            System.out.println("非法token!");
        }

    }
}
