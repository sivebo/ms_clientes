package com.sivebo.ms_clientes.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sivebo.ms_clientes.dto.request.ClienteContactoUpdateDTO;
import com.sivebo.ms_clientes.dto.request.ClienteRequestDTO;
import com.sivebo.ms_clientes.dto.response.ClienteResponseDTO;
import com.sivebo.ms_clientes.exception.DuplicateResourceException;
import com.sivebo.ms_clientes.exception.EntityNotFoundException;
import com.sivebo.ms_clientes.model.Cliente;
import com.sivebo.ms_clientes.model.TipoDocumento;
import com.sivebo.ms_clientes.repository.ClienteRepository;
import com.sivebo.ms_clientes.repository.TipoDocumentoRepository;
import com.sivebo.ms_clientes.utils.MapToDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService extends MapToDTO {

        private final ClienteRepository clienteRepository;
        private final TipoDocumentoRepository tipoDocumentoRepository;

        // RF-10: registrar cliente remitente/destinatario
        public ClienteResponseDTO create(ClienteRequestDTO dto) {
                if (clienteRepository.existsByNroDocumento(dto.getNroDocumento())) {
                        throw new DuplicateResourceException(
                                        "Ya existe un cliente con el número de documento: " + dto.getNroDocumento());
                }

                TipoDocumento tipoDocumento = tipoDocumentoRepository.findByCodigo(dto.getCodigoTipoDocumento())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Tipo de documento no encontrado: " + dto.getCodigoTipoDocumento()));

                Cliente cliente = new Cliente(
                                null,
                                tipoDocumento,
                                dto.getNroDocumento(),
                                dto.getNombre(),
                                dto.getApellido(),
                                dto.getEmail(),
                                dto.getTelefono());

                log.info(">>> Cliente creado: {} {} ({})", dto.getNombre(), dto.getApellido(), dto.getNroDocumento());
                return mapClienteToDTO(clienteRepository.save(cliente));
        }

        // RF-11: buscar cliente existente por tipo y número de documento
        public Optional<ClienteResponseDTO> getByTipoYNumeroDocumento(String codigoTipoDoc, String nroDocumento) {
                log.info(">>> Buscando cliente por tipoDocumento={}, nroDocumento={}", codigoTipoDoc, nroDocumento);
                return clienteRepository
                                .findByTipoDocumentoCodigoAndNroDocumento(codigoTipoDoc, nroDocumento)
                                .map(this::mapClienteToDTO);
        }

        public Optional<ClienteResponseDTO> getById(Long id) {
                return clienteRepository.findById(id).map(this::mapClienteToDTO);
        }

        // RF-12: actualizar datos de contacto (email, telefono)
        public Optional<ClienteResponseDTO> actualizarContacto(Long id, ClienteContactoUpdateDTO dto) {
                return clienteRepository.findById(id).map(cliente -> {
                        cliente.setEmail(dto.getEmail());
                        cliente.setTelefono(dto.getTelefono());
                        log.info(">>> Datos de contacto actualizados para cliente id={}", id);
                        return mapClienteToDTO(clienteRepository.save(cliente));
                });
        }

        // RF-13: listar clientes con paginación y filtro por nombre o numero de documento
        public Page<ClienteResponseDTO> listar(String filtro, Pageable pageable) {
                Page<Cliente> page = (filtro != null && !filtro.isBlank())
                                ? clienteRepository.findByNombreContainingIgnoreCaseOrNroDocumentoContainingIgnoreCase(
                                                filtro, filtro, pageable)
                                : clienteRepository.findAll(pageable);
                return page.map(this::mapClienteToDTO);
        }
}
