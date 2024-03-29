package com.peters.notificationservice.consumer;

import com.peters.notificationservice.dto.EmailNotificationDto;
import com.peters.notificationservice.dto.NotificationType;
import com.peters.notificationservice.dto.OrderMessageNotification;
import com.peters.notificationservice.dto.PasswordResetDto;
import com.peters.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    private final EmailService service;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(EmailNotificationDto messageDto) throws MessagingException, UnsupportedEncodingException {
        log.info(String.format("Received message -> %s ", messageDto.toString()));
        if (messageDto.getType().equals(NotificationType.VERIFICATION)) {
            executeWithRetry(() -> {
                try {
                    service.sendVerificationEmail(messageDto);
                    log.info("email verification notification {} ", messageDto.toString());
                } catch (MessagingException e) {
                    log.warn("email verification notification {} ", e.getMessage());
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (messageDto.getType().equals(NotificationType.RESEND_VERIFICATION)) {
            executeWithRetry(() -> {
                try {
                    log.info("email verification notification {} ", messageDto.toString());
                    service.reSendVerificationEmail(messageDto);
                } catch (MessagingException e) {
                    log.warn("Cause of error for resend email verification notification {} ", e.getMessage());
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.reset-password}")
    public void resetPasswordConsumeMessage(PasswordResetDto message) throws MessagingException, UnsupportedEncodingException {
        log.info(String.format("Received message -> %s ", message.toString()));
        executeWithRetry(() -> {
            try {
                log.info("email verification notification {} ", message.toString());
                service.sendResetPasswordEmail(message);
            } catch (MessagingException e) {
                log.warn("Cause of error for resetPassword notification {} ", e.getMessage());
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.login-alert}"})
    public void sendLoginAlert(EmailNotificationDto message) throws MessagingException, UnsupportedEncodingException {
        log.info(String.format("Received message -> %s ", message.toString()));
        executeWithRetry(() -> {
            try {
                log.info("account login notification {} ", message.toString());
                service.sendLoginNotification(message);
            } catch (MessagingException e) {
                log.warn("Cause of error for resetPassword notification {} ", e.getMessage());
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.order}"})
    public void sendOrderNotification(OrderMessageNotification message) throws MessagingException, UnsupportedEncodingException {
        log.info(String.format("Received message -> %s ", message.toString()));
        executeWithRetry(() -> {
            try {
                log.info("product order notification {} ", message.toString());
                service.sendOrderSummaryEmail(message);
            } catch (MessagingException e) {
                log.warn("Cause of error for resetPassword notification {} ", e.getMessage());
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void executeWithRetry(Runnable operation) {
        try {
            RetryCallback<Void, Exception> retryCallback = context -> {
                operation.run();
                return null;
            };
            new RetryTemplate().execute(retryCallback);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
