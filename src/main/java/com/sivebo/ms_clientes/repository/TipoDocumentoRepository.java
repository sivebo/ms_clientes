package com.sivebo.ms_clientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_clientes.model.TipoDocumento;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

        Optional<TipoDocumento> findByCodigo(String codigo);

        Boolean existsByCodigo(String codigo);
}
