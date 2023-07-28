package com.peters.Epay.event;

import com.peters.Epay.product.dto.OrderMessageNotification;
import com.peters.Epay.user.dto.EmailNotificationDto;
import com.peters.Epay.user.dto.PasswordResetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.key}")
    private String bindingKey;

    @Value("${rabbitmq.exchange.login-alert}")
    private String loginAlertExchange;

    @Value("${rabbitmq.binding.login-alert-key}")
    private String loginAlertKey;

    @Value("${rabbitmq.exchange.reset-password}")
    private String resetPasswordExchange;

    @Value("${rabbitmq.binding.reset-password-key}")
    private String resetPasswordBindingKey;

    @Value("${rabbitmq.exchange.order}")
    private String orderExchange;

    @Value("${rabbitmq.binding.order}")
    private String orderKey;

    public void sendNotification(EmailNotificationDto user) {
        executeWithRetry(() -> rabbitTemplate.convertAndSend(exchange, bindingKey, user));
    }

    public void sendLoginNotification(EmailNotificationDto message){
        executeWithRetry(()->rabbitTemplate.convertAndSend(loginAlertExchange, loginAlertKey, message));
    }

    public void sendResetPasswordNotification(PasswordResetDto message) {
        executeWithRetry(() -> rabbitTemplate.convertAndSend(resetPasswordExchange, resetPasswordBindingKey, message));
    }

    public void sendOrderNotification(OrderMessageNotification message){
        executeWithRetry(() -> rabbitTemplate.convertAndSend(orderExchange, orderKey, message));
    }

    private void executeWithRetry(Runnable operation) {
        try {
            RetryCallback<Void, Exception> retryCallback = context -> {
                operation.run();
                return null;
            };
            retryTemplate.execute(retryCallback);
        } catch (Exception e) {
            log.error("Failed to publish message: {}", e.getMessage());
        }
    }

}

