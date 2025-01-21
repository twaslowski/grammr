package com.grammr.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  @Bean
  public Queue morphologicalAnalysisCompleteQueue(@Value("${analysis.messaging.queue}") String queueName) {
    return new Queue(queueName);
  }

  @Bean
  public TopicExchange topicExchange(@Value("${analysis.messaging.exchange}") String exchange) {
    return new TopicExchange(exchange);
  }

  @Bean
  public Binding binding(Queue queue, TopicExchange topicExchange,
                         @Value("${analysis.messaging.routing-key.complete}") String routingKey) {
    return BindingBuilder.bind(queue)
        .to(topicExchange)
        .with(routingKey);
  }
}
