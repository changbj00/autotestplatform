package com.autotestplatform.controller;

import com.autotestplatform.utils.RedisKey;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import com.sun.scenario.effect.impl.prism.ps.PPStoPSWDisplacementMapPeer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/user")
public class SendEmailController {
    @Autowired
    private JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private RedisKey redisKey;

    public int code() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return Integer.parseInt(result);
    }

    @PostMapping(value = "/sendSimpleEmail")
    public RestApiResult sendSimpleEmail(String email) {
        String emailKey="message:"+email;
        if (redisKey.hasKey(emailKey)) {
            return restApiResult.build(RequestResultEnum.sendemail.getCode(), RequestResultEnum.sendemail.getMsg());
        }
        try {
            int code = code();
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setSubject("--email--");
            mailMessage.setText("验证码：" + code);
            jms.send(mailMessage);
            redisKey.setKey(emailKey, code);
            return restApiResult.success();
        } catch (MailException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return restApiResult.faild();
        }
    }

    public static void main(String[] args) {
        SendEmailController sendEmailController = new SendEmailController();
        System.out.println(sendEmailController.code());
    }
}
