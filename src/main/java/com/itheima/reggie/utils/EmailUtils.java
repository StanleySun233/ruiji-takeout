package com.itheima.reggie.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {

    public static void sendEmail(String toEmail, String subject, String messageText) throws MessagingException {
        // 设置SMTP服务器的属性
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com");  // SMTP服务器地址
        props.put("mail.smtp.auth", "true");    // 是否需要认证
        props.put("mail.smtp.port", "25");      // SMTP端口号，网易邮箱使用的是25端口
        props.put("mail.smtp.starttls.enable", "true");

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("stanleysun233@163.com", "KLOSZMAEVHJBBEYI");
            }
        });

        try {
            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("stanleysun233@163.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageText);

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MessagingException("邮件发送失败：" + e.getMessage());
        }
    }
}
