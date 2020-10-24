package com.charli.wcpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author charles
 */
@SpringBootApplication
@MapperScan("com.charli.wcpay.mapper")
// 开启事务管理
@EnableTransactionManagement
public class WcpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WcpayApplication.class, args);
    }

}
