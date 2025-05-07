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

    //Propertiesで設定された値
    @Value("${spring.mail.username}")
    private String from;

    public String sendMail(String email) {

        Random random = new Random();

        //111111~999998の認証番号生成
        int certify = random.nextInt(888888) + 111111;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); //メール発信者
        message.setTo(email); //メール受信者
        message.setSubject("認証番号"); //メールのタイトル
        String content = "認証番号は : " + certify;
        message.setText(content); //メールの内容
        message.setSentDate(new Date()); //メール発信時間

        mailSender.send(message); //メール発信

        String num = Integer.toString(certify);

        return num;

    }


}
