package com.peters.User_Registration_and_Email_Verification.event;

import com.peters.User_Registration_and_Email_Verification.user.dto.EmailNotificationDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.PasswordResetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQPublisher{
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.binding.key}")
    private String bindingKey;

    @Value("${rabbitmq.exchange.reset-password}")
    private String resetPasswordExchange;
    @Value("${rabbitmq.binding.reset-password-key}")
    private String resetPasswordBindingKey;


    public void sendNotification(EmailNotificationDto user){
        rabbitTemplate.convertAndSend(exchange, bindingKey, user);
    }

    public void sendResetPasswordNotification(PasswordResetDto message){
        rabbitTemplate.convertAndSend(resetPasswordExchange, resetPasswordBindingKey, message);
    }

}
