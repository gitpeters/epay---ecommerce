package com.peters.User_Registration_and_Email_Verification.event;

import io.lettuce.core.dynamic.intercept.MethodInterceptor;
import io.lettuce.core.dynamic.intercept.MethodInvocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.CorrelationDataPostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.*;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

//@Component
//@Slf4j
//public class RabbitMQRetryCallBack implements RabbitTemplate.ConfirmCallback, CorrelationDataPostProcessor, MethodInterceptor {
//    private final RabbitTemplateWrapper rabbitTemplateWrapper;
//    // ...
//
//    @Autowired
//    public RabbitMQRetryCallBack(RabbitTemplateWrapper rabbitTemplateWrapper) {
//        this.rabbitTemplateWrapper = rabbitTemplateWrapper;
//    }
//    private static final int MAX_RETRY_ATTEMPTS = 3;
//    private static final long INITIAL_RETRY_DELAY = 5000; // 5 seconds
//    private static final long MAX_RETRY_DELAY = 60000; // 1 minute
//
//
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (!ack) {
//            // Message failed to be delivered, handle retry logic
//            retryMessage(correlationData.getReturned(), correlationData);
//        }
//    }
//    private void retryMessage(Object message, CorrelationData originalCorrelationData) {
//        RetryTemplate retryTemplate = createRetryTemplate();
//        // Resend the message to the retry queue
//        rabbitTemplateWrapper.convertAndSend("retry-exchange", "retry-routing-key", message, (MessagePostProcessor) this);
//
//        // Optionally, you can implement a delay or backoff strategy before retrying
//        RetryCallback<Object, RuntimeException> retryCallback = context -> {
//            log.info("Retrying message: {}", context.getAttribute("message"));
//            rabbitTemplateWrapper.convertAndSend("retry-exchange", "retry-routing-key", context.getAttribute("message"), (MessagePostProcessor) this);
//            throw new RuntimeException("Failed to send message");
//        };
//
//        try {
//            retryTemplate.execute(retryCallback, (RecoveryCallback<Object>) null);
//        } catch (RuntimeException e) {
//            log.error("Failed to send message after retries: {}", originalCorrelationData.getReturned());
//            // Handle the failure (e.g., logging, error handling, etc.)
//        }
//    }
//    @Override
//    public CorrelationData postProcess(Message message, CorrelationData correlationData) {
//        // Store the original message in the correlation data for retry purposes
//        message.getMessageProperties().setHeader("originalMessage", message.getBody());
//        return correlationData;
//    }
//
//    @Override
//    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
//        try{
//            return methodInvocation.proceed();
//        }catch (Exception ex) {
//            RetryOperations retryOperations = createRetryTemplate();
//            return retryOperations.execute(context -> methodInvocation.proceed(), new RecoveryCallback<Object>() {
//                @Override
//                public Object recover(RetryContext context) throws Exception {
//                    log.error("Failed to send message after retries: {}", context.getAttribute("message"));
//                    // Handle the failure (e.g., logging, error handling, etc.)
//                    return null; // or throw an exception if needed
//                }
//            });
//        }
//    }
//
//    private RetryTemplate createRetryTemplate() {
//        RetryTemplate retryTemplate = new RetryTemplate();
//        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//        backOffPolicy.setBackOffPeriod(INITIAL_RETRY_DELAY);
//        retryTemplate.setBackOffPolicy(backOffPolicy);
//        retryTemplate.setRetryPolicy(createRetryPolicy());
//        return retryTemplate;
//    }
//
//    private RetryPolicy createRetryPolicy() {
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//        retryPolicy.setMaxAttempts(MAX_RETRY_ATTEMPTS);
//        return retryPolicy;
//    }
//}
