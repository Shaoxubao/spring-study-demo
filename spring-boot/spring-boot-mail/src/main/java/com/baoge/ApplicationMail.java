package com.baoge;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class ApplicationMail {
    public static void main(String[] args) {
        SimpleEmail email = new SimpleEmail();
        try {
            // 设置SMTP服务器
            email.setHostName("smtp.163.com");
            // 设置SSL端口，对于网易邮箱需要使用SSL端口465或TLS端口587
            email.setSmtpPort(465); // 或者使用email.setSslSmtpPort("smtp.163.com", 465);
            email.setSSLOnConnect(true); // 使用SSL连接
            // 或者使用email.setTLS(true); // 使用TLS连接，如果你选择587端口的话

            // 设置发件人邮箱地址和密码（注意：密码不是邮箱登录密码，而是通过网页邮箱获取的SMTP服务密码）
            email.setAuthenticator(new DefaultAuthenticator("xubao_shao@163.com", "BDUdeQkQ82vvyiKv"));
            email.setFrom("xubao_shao@163.com");
            email.setSubject("测试邮件");
            email.setMsg("这是一封测试邮件！");
            email.addTo("xubao_shao@163.com"); // 接收者邮箱地址

            // 发送邮件
            email.send();
            System.out.println("邮件发送成功！");
        } catch (EmailException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
    }
}
