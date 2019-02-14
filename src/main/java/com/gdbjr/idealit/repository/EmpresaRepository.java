package com.gdbjr.idealit.repository;

import com.gdbjr.idealit.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    public Empresa findByCnpjAndEnabled(String cnpj, String enabled);

    public List<Empresa> findAllByEnabled(String enabled);

}
