package com.sivebo.ms_clientes.service;

import com.sivebo.ms_clientes.dto.ClienteRequest;
import com.sivebo.ms_clientes.dto.ClienteResponse;
import com.sivebo.ms_clientes.model.Cliente;
import com.sivebo.ms_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        log.info("[ms_clientes] Creando cliente con RUT={}", request.getRut());
        if (clienteRepository.existsByRut(request.getRut())) {
            log.warn("[ms_clientes] RUT ya registrado: {}", request.getRut());
            throw new RuntimeException("Ya existe un cliente con el RUT: " + request.getRut());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && clienteRepository.existsByEmail(request.getEmail())) {
            log.warn("[ms_clientes] Email ya registrado: {}", request.getEmail());
            throw new RuntimeException("Ya existe un cliente con el email: " + request.getEmail());
        }
        ClienteResponse response = toResponse(clienteRepository.save(Cliente.builder()
                .nombre(request.getNombre())
                .rut(request.getRut())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .build()));
        log.info("[ms_clientes] Cliente creado exitosamente id={}", response.getId());
        return response;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        log.info("[ms_clientes] Listando todos los clientes");
        List<ClienteResponse> lista = clienteRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
        log.info("[ms_clientes] Total clientes: {}", lista.size());
        return lista;
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        log.info("[ms_clientes] Buscando cliente id={}", id);
        return toResponse(clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ms_clientes] Cliente no encontrado id={}", id);
                    return new RuntimeException("Cliente no encontrado con ID: " + id);
                }));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarPorNombre(String nombre) {
        log.info("[ms_clientes] Buscando clientes por nombre: {}", nombre);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        log.info("[ms_clientes] Actualizando cliente id={}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ms_clientes] Cliente no encontrado para actualizar id={}", id);
                    return new RuntimeException("Cliente no encontrado con ID: " + id);
                });
        if (!cliente.getRut().equals(request.getRut())
                && clienteRepository.existsByRut(request.getRut())) {
            log.warn("[ms_clientes] RUT ya en uso: {}", request.getRut());
            throw new RuntimeException("El RUT ya está en uso: " + request.getRut());
        }
        cliente.setNombre(request.getNombre());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        log.info("[ms_clientes] Cliente actualizado exitosamente id={}", id);
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("[ms_clientes] Eliminando cliente id={}", id);
        if (!clienteRepository.existsById(id)) {
            log.warn("[ms_clientes] Cliente no encontrado para eliminar id={}", id);
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
        log.info("[ms_clientes] Cliente eliminado id={}", id);
    }

    private ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .rut(c.getRut())
                .telefono(c.getTelefono())
                .email(c.getEmail())
                .build();
    }
}
