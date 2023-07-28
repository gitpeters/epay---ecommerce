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

    @Value("${rabbitmq.queue.login-alert}")
    private String loginAlertQueue;
    @Value("${rabbitmq.exchange.login-alert}")
    private String loginAlertExchange;
    @Value("${rabbitmq.binding.login-alert-key}")
    private String loginAlertBindingKey;
    @Value("${rabbitmq.queue.order}")
    private String orderQueue;
    @Value("${rabbitmq.exchange.order}")
    private String orderExchange;
    @Value("${rabbitmq.binding.order}")
    private String orderBindingKey;

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
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }


    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(bindingKey);
    }

    @Bean
    public Queue resetPaswordQueue() {
        return new Queue(resetPasswordQueue);
    }

    @Bean
    public TopicExchange resetPasswordExchange() {
        return new TopicExchange(resetPasswordExchange);
    }

    @Bean
    public Binding resetPasswordBinding() {
        return BindingBuilder
                .bind(resetPaswordQueue())
                .to(resetPasswordExchange())
                .with(resetPasswordBindingKey);
    }

    @Bean
    public Queue loginAlertQueue(){
        return new Queue(loginAlertQueue);
    }

    @Bean
    public TopicExchange loginAlertExchange(){
        return new TopicExchange(loginAlertExchange);
    }

    @Bean
    public Binding loginBinding(){
        return BindingBuilder
                .bind(loginAlertQueue())
                .to(loginAlertExchange())
                .with(loginAlertBindingKey);
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(orderQueue);
    }

    @Bean
    public TopicExchange orderExchange(){
        return new TopicExchange(orderExchange);
    }

    @Bean
    public Binding orderBinding(){
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(orderBindingKey);
    }
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}