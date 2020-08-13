package com.autotestplatform.controller;

import com.autotestplatform.utils.RestApiResult;
import com.autotestplatform.utils.SendEmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 发送邮件控制器
 * */
@Slf4j
@RestController
@RequestMapping("/user")
public class SendEmailController {
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private SendEmailUtil sendEmailUtil;

    @PostMapping(value = "/sendEmailCode")
    public RestApiResult sendEmailCode(String email){
        log.info("输入参数：{}",email);
        sendEmailUtil.sendSimpleEmail(email);
        return restApiResult.success();
    }

}
