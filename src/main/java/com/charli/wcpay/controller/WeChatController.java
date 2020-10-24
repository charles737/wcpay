package com.charli.wcpay.controller;

import com.charli.wcpay.config.WeChatConfig;
import com.charli.wcpay.domain.JsonData;
import com.charli.wcpay.domain.User;
import com.charli.wcpay.domain.VideoOrder;
import com.charli.wcpay.service.UserService;
import com.charli.wcpay.service.VideoOrderService;
import com.charli.wcpay.utils.JwtUtils;
import com.charli.wcpay.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping("/api/v1/wechat")
public class WeChatController {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 拼装微信扫一扫登陆url
     * @return
     */
    @GetMapping("login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true)String accessPage) throws UnsupportedEncodingException {

        // 获取开放平台重定向地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl();

        // 进行编码
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");

        String qrcodeUrl = String.format(weChatConfig.getOpenQrcodeUrl(),weChatConfig.getOpenAppid(),callbackUrl,accessPage);

        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 微信扫码登陆，回调地址
     * @param code
     * @param state
     * @param response
     * @throws IOException
     */
    @GetMapping("/user/callback")
    public void wechatUserCallback(@RequestParam(value = "code", required = true) String code,
                                   String state, HttpServletResponse response ) throws IOException {

        User user = userService.saveWeChatUser(code);
        if (user != null){
            // 生成jwt
            String token = JwtUtils.geneJsonWebToken(user);
            // state 当前用户的页面地址，需要拼接http://，这样才不会站内跳转
            response.sendRedirect(state+"?token="+token+"&head_img="+user.getHeadImg()+"&name="+URLEncoder.encode(user.getName(), "UTF-8"));
        }

    }

    /**
     * 微信支付回调
     */
    @RequestMapping("/order/callback")
    public void orderCallback(HttpServletResponse response, HttpServletRequest request) throws Exception {

        InputStream inputStream = request.getInputStream();
        // BufferReader是包装设计模式，性能更高
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        // StringBuffer进行缓冲
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null){
            sb.append(line);
        }

        br.close();
        inputStream.close();

        Map<String, String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callbackMap.toString());

        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callbackMap);

        // 判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())){

            if ("SUCCESS".equals(sortedMap.get("result_code"))){

                String outTradeNo = sortedMap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);

                if (dbVideoOrder != null && dbVideoOrder.getState() == 0){ // 判断逻辑看业务场景

                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    // 影响行数
                    int rows = videoOrderService.updateVideoOrderByOutTradeNo(videoOrder);
                    if (rows == 1){ // 通知微信订单处理成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }
        // 处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
    }

}
