package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail.default")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailDefault {
    private String destino;
    private String asunto;
    private String texto;

}
