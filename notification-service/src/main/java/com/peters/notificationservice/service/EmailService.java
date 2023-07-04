package com.peters.notificationservice.service;

import com.peters.notificationservice.dto.EmailNotificationDto;
import com.peters.notificationservice.dto.PasswordResetDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendVerificationEmail(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Central Estore Service";
        String mailContent = "<p> Hi, "+ messageDto.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us. "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +messageDto.getUrl()+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(messageDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void reSendVerificationEmail(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification - Resent";
        String senderName = "Central Estore Service";
        String mailContent = "<p> Hi, "+ messageDto.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us. "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +messageDto.getUrl()+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(messageDto.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(PasswordResetDto passwordReset) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset";
        String senderName = "Central Estore Service";
        String mailContent = "<p> Hi, "+ passwordReset.getFirstName()+ ", </p>"+
                "<p>Below is the token to reset your password. "+"" +
                "If you did not initiate this request, kindly contact admin at <b>info@techiebros.come</b>.</p>"+
                "<h2 style='color: #057d25; letter-spacing: 0.1em'>"+passwordReset.getToken()+"</h2>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(passwordReset.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
