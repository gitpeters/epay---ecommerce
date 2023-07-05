package com.peters.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peters.notificationservice.dto.EmailNotificationDto;
import com.peters.notificationservice.dto.NotificationType;
import com.peters.notificationservice.dto.PasswordResetDto;
import com.peters.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.TimeUnit;

import static org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType.CORRELATED;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private EmailService emailService;
    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.binding.key}")
    private String bindingKey;

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.queue.reset-password}")
    private String resetPasswordQueue;
    @Value("${rabbitmq.exchange.reset-password}")
    private String resetPasswordExchange;
    @Value("${rabbitmq.binding.reset-password-key}")
    private String resetPasswordBindingKey;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirmType(CORRELATED);
        return connectionFactory;
    }
    @Bean
    public Queue queue(){
        return new Queue(queue);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(bindingKey);
    }

    @Bean
    public Queue resetPaswordQueue(){
        return new Queue(resetPasswordQueue);
    }
    @Bean
    public TopicExchange resetPasswordExchange(){
        return new TopicExchange(resetPasswordExchange);
    }
    @Bean
    public Binding resetPasswordBinding(){
        return BindingBuilder
                .bind(resetPaswordQueue())
                .to(resetPasswordExchange())
                .with(resetPasswordBindingKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    // handled failed message
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange("dlx_exchange");
    }

    @Bean
    public Queue dlq() {
        return QueueBuilder.durable("dlq_queue")
                .withArgument("x-dead-letter-exchange", exchange)
                .withArgument("x-dead-letter-routing-key", bindingKey)
                .build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq())
                .to(dlxExchange())
                .with("#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            String failedMessageKey = generateFailedMessageKey(returnedMessage.getMessage());
            redisTemplate.opsForValue().set(failedMessageKey, returnedMessage.getMessage());
            rabbitTemplate.convertAndSend(returnedMessage.getExchange(),
                    returnedMessage.getRoutingKey(), returnedMessage.getMessage());
        });


        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                String failedMessageKey = correlationData.getId();
                redisTemplate.delete(failedMessageKey);
            }
        });

        return rabbitTemplate;
    }

    private String generateFailedMessageKey(Message message) {
        // Generate a unique key for the failed message based on message properties
        MessageProperties messageProperties = message.getMessageProperties();
        String messageId = messageProperties.getMessageId();
        String exchange = messageProperties.getReceivedExchange();
        String routingKey = messageProperties.getReceivedRoutingKey();

        return String.format("failed-message:%s:%s:%s", exchange, routingKey, messageId);
    }





    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000L); // Configure the delay between retries (in milliseconds)
        retryTemplate.setBackOffPolicy(backOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Configure the maximum number of retry attempts
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
