package com.rastreamento.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @Bean
    public Queue filaTempoReal() {
        return new Queue("fila-tempo-real", true);
    }

    @Bean
    public Queue filaHistorico() {
        return new Queue("fila-historico", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bindingTempoReal(Queue filaTempoReal, DirectExchange exchange) {
        return BindingBuilder
                .bind(filaTempoReal)
                .to(exchange)
                .with(routingKey);
    }

    @Bean
    public Binding bindingHistorico(Queue filaHistorico, DirectExchange exchange) {
        return BindingBuilder
                .bind(filaHistorico)
                .to(exchange)
                .with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
} 