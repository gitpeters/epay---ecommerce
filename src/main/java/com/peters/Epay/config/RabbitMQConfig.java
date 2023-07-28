package com.peters.Epay.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import static org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType.CORRELATED;
/**
 How to capture failed messages and retry them in rabbitmq
 1) Enable publisher confirms: Publisher confirms allow you to receive acknowledgments from RabbitMQ when a message is successfully published or not. Enable publisher confirms when creating your RabbitMQ connection.

 2) Set up a dead letter exchange (DLX): A dead letter exchange is used to capture and handle failed messages. You can create a dead letter exchange and bind it to a queue where the failed messages will be routed.

 3) Create a retry queue: Create a retry queue where the failed messages will be stored temporarily before being retried.

 4) Configure message expiration: Set an expiration time for messages in the retry queue. This ensures that the messages are not stuck in the retry queue indefinitely.

 5) Implement retry logic: When a message fails to be delivered, handle the publisher confirm event and resend the message to the retry queue. You can use a delay or backoff strategy between retries to avoid overwhelming the system.

 6) Configure the dead letter routing: Bind the retry queue to the dead letter exchange with a routing key that matches the original message's routing key.

 7) Consume and process messages from the retry queue: Set up a consumer that reads messages from the retry queue and attempts to process them. If the processing is successful, acknowledge the message. Otherwise, handle the failure and decide whether to retry or discard the message.
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.binding.key}")
    private String bindingKey;

    @Value("${rabbitmq.queue.reset-password}")
    private String resetPasswordQueue;
    @Value("${rabbitmq.exchange.reset-password}")
    private String resetPasswordExchange;
    @Value("${rabbitmq.binding.reset-password-key}")
    private String resetPasswordBindingKey;

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

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
    public ConnectionFactory rabbitmqConnectionFactory() {
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
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConnectionFactory(rabbitmqConnectionFactory());
        return rabbitTemplate;
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
