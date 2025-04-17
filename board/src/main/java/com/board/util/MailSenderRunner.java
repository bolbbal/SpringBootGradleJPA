package com.board.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MailSenderRunner {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public String sendMail(String email) {

        Random random = new Random();

        int certify = random.nextInt(888888) + 111111;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("認証番号");
        String content = "認証番号は : " + certify;
        message.setText(content);
        message.setSentDate(new Date());

        mailSender.send(message);

        String num = Integer.toString(certify);

        return num;

    }


}
