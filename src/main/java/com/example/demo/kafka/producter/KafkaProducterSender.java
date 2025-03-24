package com.example.demo.kafka.producter;

import com.example.demo.dto.BodyMail;
import com.example.demo.utils.MailDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducterSender {
    @Autowired
    private KafkaTemplate<String, BodyMail> kafkaTemplate;
    @Autowired
    private MailDefault mailConfig;

    public void send(String topic, String message) {
        log.info("Enviando : {}", message);
        log.info("--------------------------------");

        BodyMail body=BodyMail.builder()
                .texto(mailConfig.getTexto())
                .asunto(mailConfig.getAsunto())
                .destino(mailConfig.getDestino())
                .build();

        kafkaTemplate.send(topic,body);
    }
}
