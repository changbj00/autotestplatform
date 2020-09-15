package com.autotestplatform.controller;

import com.autotestplatform.entity.User;
import com.autotestplatform.hander.CatchException;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import com.autotestplatform.utils.SendEmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

/**
 * 发送邮件控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class SendEmailController {
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private SendEmailUtil sendEmailUtil;
    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/sendEmailCode")
    public RestApiResult sendEmailCode(@Email String email) {
        log.info("输入参数：{}", email);
        User user = userMapper.getUser(email);
        if (user != null) {
            sendEmailUtil.sendSimpleEmail(email);
            return restApiResult.success();
        } else
            throw new CatchException(RequestResultEnum.forgot);
    }

}
