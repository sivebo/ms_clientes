package com.sivebo.ms_clientes.repository;

import com.sivebo.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
