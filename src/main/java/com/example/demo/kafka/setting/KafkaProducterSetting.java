package com.example.demo.kafka.setting;

import com.example.demo.dto.BodyMail;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase se encarga de la configuracion propia de el productor donde informare ante fallos en la creacion de factura
 * Las configuraciones se realizan sin el properties actualmente
 * Utilizando un Bean para settear la configuracion
 * TODO MODIFICAR PARA UTILIZACION DE DATOS VIA PROPERTIES
 */

@Configuration
public class KafkaProducterSetting {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public DefaultKafkaProducerFactory<String, BodyMail> kafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, BodyMail> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducer());
    }

    @PreDestroy

public void destroy() {
kafkaProducer().destroy();

    }
}
