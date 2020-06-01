package kz.danke.rabbitmqdemoproject.configuration.rabbitmq;

import kz.danke.rabbitmqdemoproject.service.consumer.ReceiveMessageHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "my-exchange";
    public static final String QUEUE_NAME = "my-queue";
    public static final String EXIST_EXCHANGE_NAME = "exist-exchange";
    public static final String EXIST_QUEUE = "exist-queue";

    @Bean
    public Queue rabbitQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(@Qualifier("rabbitQueue") Queue queue,
                           @Qualifier("exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my-tag.#");
    }

    @Bean
    public MessageListenerAdapter adapter(ReceiveMessageHandler messageHandler) {
        return new MessageListenerAdapter(messageHandler, "handleMessage");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter adapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(EXIST_QUEUE);
        container.setMessageListener(adapter);

        return container;
    }
}
