package com.peters.User_Registration_and_Email_Verification.async;

import com.peters.User_Registration_and_Email_Verification.event.RabbitMQPublisher;
import com.peters.User_Registration_and_Email_Verification.user.dto.EmailNotificationDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.PasswordResetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncRunner {
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public AsyncRunner(RabbitMQPublisher rabbitMQPublisher) {
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @Async
    public void sendNotification(EmailNotificationDto emailNotificationDto){
        rabbitMQPublisher.sendNotification(emailNotificationDto);
    }

    @Async
    public void sendResetPasswordNotification(PasswordResetDto resetPasswordDto){
        rabbitMQPublisher.sendResetPasswordNotification(resetPasswordDto);
    }

}
