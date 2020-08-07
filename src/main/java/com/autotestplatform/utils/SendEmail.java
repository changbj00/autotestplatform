package com.autotestplatform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;
@Component
public class SendEmail {
    @Autowired
    private JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    TemplateEngine templateEngine;
    public String code(){
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }
    public String sendSimpleEmail(String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setSubject("--email--");
            mailMessage.setText("验证码：");
            mailMessage.setText(code());
            jms.send(mailMessage);
            return "发送成功";
        } catch (MailException e) {
            e.printStackTrace();
            return e.getMessage();
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
