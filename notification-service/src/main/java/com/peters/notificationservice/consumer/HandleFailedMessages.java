package com.peters.notificationservice.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class HandleFailedMessages {
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private long failedMessagesRetryDelay  = 1000l;

    @RabbitListener(queues = "dlq_queue")
    public void handleFailedMessage(Message failedMessage) {
        String failedMessageKey = generateFailedMessageKey(failedMessage);
        MessageProperties messageProperties = failedMessage.getMessageProperties();

        rabbitTemplate.convertAndSend(messageProperties.getReceivedExchange(), messageProperties.getReceivedRoutingKey(),
                failedMessage, new CorrelationData(failedMessageKey));
        failedMessagesRetryDelay *=2;
        redisTemplate.expire(failedMessageKey, failedMessagesRetryDelay, TimeUnit.MILLISECONDS);
    }

    private String generateFailedMessageKey(Message message) {
        // Generate a unique key for the failed message based on message properties
        MessageProperties messageProperties = message.getMessageProperties();
        String messageId = messageProperties.getMessageId();
        String exchange = messageProperties.getReceivedExchange();
        String routingKey = messageProperties.getReceivedRoutingKey();

        return String.format("failed-message:%s:%s:%s", exchange, routingKey, messageId);
    }
}
