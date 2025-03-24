package com.example.demo.kafka.producter.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Creacion del Topico para emitir mensajes . El mismo es generado por el productor instanciado en @KafkaProducterSetting.java
 *
 */
@Configuration
public class KafkaProducterTopic {

    private final String topicErrorF="error_facturacion";
    @Bean
    public NewTopic topicErrorFacturacion(){
//        return new NewTopic(topicErrorF, 1, (short) 1);

        return TopicBuilder.name(topicErrorF).build();
    }

}
