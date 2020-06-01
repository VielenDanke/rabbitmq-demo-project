package kz.danke.rabbitmqdemoproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kz.danke.rabbitmqdemoproject.configuration.rabbitmq.RabbitMQConfiguration.EXIST_EXCHANGE_NAME;
import static kz.danke.rabbitmqdemoproject.configuration.rabbitmq.RabbitMQConfiguration.EXIST_QUEUE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SendMessageController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam(name = "message") String message) {
        rabbitTemplate.convertAndSend(EXIST_EXCHANGE_NAME, "exist-tag.topic", message);

        return new ResponseEntity<>(String.format("Message had been sent %s", message), HttpStatus.CREATED);
    }

    @GetMapping
    public void receiveMessage() {
        String message = (String) rabbitTemplate.receiveAndConvert(EXIST_QUEUE);

        log.info(message);
    }
}
