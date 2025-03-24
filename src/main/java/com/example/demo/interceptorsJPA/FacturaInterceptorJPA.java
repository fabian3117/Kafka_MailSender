package com.example.demo.interceptorsJPA;

import com.example.demo.dto.FacturaDTO;
import com.example.demo.models.FacturaModel;
import jakarta.persistence.PrePersist;

import java.sql.Date;

public class FacturaInterceptorJPA {
    @PrePersist
    public void prePersist(FacturaModel facturaModel) {
        facturaModel.setFecha(new Date(System.currentTimeMillis()));
    }
}
