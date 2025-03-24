package com.example.demo.repository;

import com.example.demo.models.FacturaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface facturaRepository extends JpaRepository<FacturaModel, Long> {


}
