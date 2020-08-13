package com.autotestplatform.utils;

import com.autotestplatform.hander.CatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;

/**
 * 发送邮件工具类
 */
@Component
public class SendEmailUtil {
    @Autowired
    private JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private RedisUtil redisUtil;
    //生成六位随机数
    public int code(){
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return Integer.parseInt(result);
    }
    //发送验证码
    public String sendSimpleEmail(String email) {
        String emailKey="message:"+email;
        if (redisUtil.hasKey(emailKey)) {
            return RequestResultEnum.sendemail.getMsg();
        }
        try {
            int code = code();
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setSubject("--email--");
            mailMessage.setText("验证码：" + code);
            jms.send(mailMessage);
            redisUtil.setKey(emailKey, code,1);
            return "发送成功";
        } catch (MailException e) {
            throw new CatchException(RequestResultEnum.SERVER_BUSY);
        }
    }

    public String sendHtmlEmail() {
        MimeMessage message = null;
        message = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo("1127701980@qq.com");
            helper.setSubject("--email--");
            StringBuffer sb = new StringBuffer("<p style='color:#6db33f'>使用Spring Boot发送HTML格式邮件。</p>");
            helper.setText(sb.toString(), true);
            jms.send(message);
            return "发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String sendAttachmentsMail() {
        MimeMessage mimeMessage = null;
        mimeMessage = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo("1127701980@qq.com");
            helper.setSubject("--email--");
            StringBuffer sb = new StringBuffer("<p style='color:#6db33f'>详情参见附件内容！</p>");
            helper.setText(sb.toString(), true);
            //传入附件
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/file/logfile.txt"));
            helper.addAttachment("logfile.txt", file);
            jms.send(mimeMessage);
            return "发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public String sendInlineMail() {
        MimeMessage mimeMessage = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo("1127701980@qq.com");
            helper.setSubject("--email--");
            StringBuffer sb = new StringBuffer("<html><body>博客图：<img src='cid:img'/></body></html>");
            helper.setText(sb.toString(), true);
            //传入附件
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/img/img1.jpg"));
            helper.addInline("img", file);
            jms.send(mimeMessage);
            return "发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String sendTemplateEmail(String code) {
        MimeMessage mimeMessage = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo("1127701980@qq.com");
            helper.setSubject("--email--");
            //处理邮件模板
            Context context = new Context();
            context.setVariable("code", code);
            String template = templateEngine.process("emailTemplate", context);
            helper.setText(template, true);
            jms.send(mimeMessage);
            return "发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
