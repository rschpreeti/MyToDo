package com.todo.AdvanceToDo.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Date;

@Service
public class EmailSenderService {


    @Autowired JavaMailSender javaMailSender;

    public void sendEmailMassege(String toEmail,String subject,String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("mytodos@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body,true);
        javaMailSender.send(message);
        System.out.println("User notified by email");
    }



    public void dailyTaskNotification(String toEmail, String body ,String subject) throws MessagingException{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("mytodos@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body,true);
        javaMailSender.send(message);
    }
}
