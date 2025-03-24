package com.example.demo.controller;

import com.example.demo.dto.FacturaDTO;
import com.example.demo.models.FacturaModel;
import com.example.demo.service.FacturaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/facturacion")
@RequiredArgsConstructor
public class FacturacionController {
    private final FacturaService facturaService;


    @GetMapping("/{userId}")
    public FacturaDTO getFacturacion(@PathVariable @NonNull Long userId) {

        return facturaService.getFacturaById(userId);

    }
    @GetMapping("/")
    public List<FacturaModel> getAllFacturas(){
        return facturaService.getAllFacturas();
    }
    @PostMapping("/{userId}")
    public void createFacturacion(@PathVariable @NonNull Long userId, @RequestBody FacturaDTO facturaDTO) {

        if(userId==5){
            throw new RuntimeException("Emulated error -> Send kafka message");
        }
        facturaService.saveNewFactura(facturaDTO);
    }
}
