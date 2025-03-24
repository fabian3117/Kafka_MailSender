package com.example.demo;

import com.example.demo.controller.FacturacionController;
import com.example.demo.dto.FacturaDTO;
import com.example.demo.service.FacturaService;
import com.example.demo.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Slf4j
public class kafkaLister  {

    @Autowired
    private FacturacionController facturacionController;

    @Autowired
    private FacturaService facturaService;
    @KafkaListener(topics = Utils.VALUE_TOPIC, groupId = "group1")
    void listener(String data) {
        //--->  Disparador de evento ante recepcion de mensaje  <---
        FacturaDTO factura= FacturaDTO.builder()
                .fecha(new Date(System.currentTimeMillis()))
                .monto(10L)
                .facturaId(Long.valueOf(data)).build();
        facturaService.saveNewFactura(factura);
        log.info("Recepcion de mensaje [{}] in group1", data);
    }

}
