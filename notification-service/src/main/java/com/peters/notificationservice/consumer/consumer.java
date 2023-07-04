package com.peters.notificationservice.consumer;

import com.peters.notificationservice.dto.EmailNotificationDto;
import com.peters.notificationservice.dto.NotificationType;
import com.peters.notificationservice.dto.PasswordResetDto;
import com.peters.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class consumer {
    private final EmailService service;
   @RabbitListener(queues = "${rabbitmq.queue.name}")
   public void consumeMessage(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
    log.info(String.format("Received message -> %s ", messageDto.toString()));
    if(messageDto.getType().equals(NotificationType.VERIFICATION)){
        service.sendVerificationEmail(messageDto);
    }
    if(messageDto.getType().equals(NotificationType.RESEND_VERIFICATION)){
        service.reSendVerificationEmail(messageDto);
    }
   }
    @RabbitListener(queues = "${rabbitmq.queue.reset-password}")
    public void resetPasswordConsumeMessage(PasswordResetDto message) throws MessagingException, UnsupportedEncodingException {
        log.info(String.format("Received message -> %s ", message.toString()));
        service.sendResetPasswordEmail(message);
    }
}
