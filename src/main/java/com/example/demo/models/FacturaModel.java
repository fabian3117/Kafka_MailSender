package com.example.demo.models;

import com.example.demo.interceptorsJPA.FacturaInterceptorJPA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@Builder
@Table
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(FacturaInterceptorJPA.class)
public class FacturaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date fecha;
    private Long facturaId;
    private Long monto;

}
