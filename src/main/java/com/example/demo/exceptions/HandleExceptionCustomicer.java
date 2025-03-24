package com.example.demo.exceptions;

import com.example.demo.kafka.producter.KafkaProducterSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleExceptionCustomicer {
    private final String topicErrorF="error_facturacion";
    @Autowired
    KafkaProducterSender kafkaProducterSender;

    Logger logger = LoggerFactory.getLogger(HandleExceptionCustomicer.class);

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());

        kafkaProducterSender.send(topicErrorF,e.getMessage());

       // kafkaProducterSender.send(topicErrorF,"Data "+System.currentTimeMillis());

    }

    public void handleRuntimeException(String e) {
        logger.error(e);
    }

}
