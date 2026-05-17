package com.sivebo.ms_clientes.service;

import com.sivebo.ms_clientes.dto.ClienteRequest;
import com.sivebo.ms_clientes.dto.ClienteResponse;
import com.sivebo.ms_clientes.model.Cliente;
import com.sivebo.ms_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByRut(request.getRut()))
            throw new RuntimeException("Ya existe un cliente con el RUT: " + request.getRut());

        if (request.getEmail() != null && !request.getEmail().isBlank()
                && clienteRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Ya existe un cliente con el email: " + request.getEmail());

        return toResponse(clienteRepository.save(Cliente.builder()
                .nombre(request.getNombre())
                .rut(request.getRut())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .build()));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return toResponse(clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id)));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        if (!cliente.getRut().equals(request.getRut())
                && clienteRepository.existsByRut(request.getRut()))
            throw new RuntimeException("El RUT ya está en uso: " + request.getRut());

        cliente.setNombre(request.getNombre());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());

        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        clienteRepository.deleteById(id);
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
