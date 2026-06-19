package com.sivebo.ms_clientes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_clientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

        Optional<Cliente> findByTipoDocumentoCodigoAndNroDocumento(String codigoTipoDoc, String nroDocumento);

        Boolean existsByNroDocumento(String nroDocumento);

        Page<Cliente> findByNombreContainingIgnoreCaseOrNroDocumentoContainingIgnoreCase(
                        String nombre, String nroDocumento, Pageable pageable);
}
