package com.peters.User_Registration_and_Email_Verification.event;

import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompletionEventListener implements ApplicationListener<RegistrationCompletePublisher> {
    private final IUserService userService;

    private UserEntity theUser;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompletePublisher event) {

        //1. Get the newly registered user
        theUser = event.getUser();
        //2. Create verification token for user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token to db
        userService.saveVerificationToken(theUser, verificationToken);
        //4. Build the verification url
        String url = event.getApplicationUrl()+"/api/v1/register/verify-email?token="+verificationToken;

        //5. Send the url to user mail
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("click this link to verify your email {}", url);

    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us, "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("depitaztech@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
