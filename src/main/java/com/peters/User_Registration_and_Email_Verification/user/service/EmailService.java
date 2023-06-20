package com.peters.User_Registration_and_Email_Verification.user.service;

import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
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
    public void sendVerificationEmail(String url, UserEntity theUser) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Central Estore Service";
        String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us. "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(Integer token, UserEntity theUser) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset";
        String senderName = "Central Estore Service";
        String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Below is the token to reset your password. "+"" +
                "If you did not initiate this request, kindly contact admin at <b>info@techiebros.come</b>.</p>"+
                "<h2 style='color: #057d25; letter-spacing: 0.1em'>"+token+"</h2>"+
                "<p> Thank you. </P> <hr> <br> <b> Central Estore Service.</b>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
