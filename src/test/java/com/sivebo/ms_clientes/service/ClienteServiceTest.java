package com.sivebo.ms_clientes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sivebo.ms_clientes.dto.request.ClienteContactoUpdateDTO;
import com.sivebo.ms_clientes.dto.request.ClienteRequestDTO;
import com.sivebo.ms_clientes.dto.response.ClienteResponseDTO;
import com.sivebo.ms_clientes.exception.DuplicateResourceException;
import com.sivebo.ms_clientes.exception.EntityNotFoundException;
import com.sivebo.ms_clientes.model.Cliente;
import com.sivebo.ms_clientes.model.TipoDocumento;
import com.sivebo.ms_clientes.repository.ClienteRepository;
import com.sivebo.ms_clientes.repository.TipoDocumentoRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock ClienteRepository clienteRepository;
    @Mock TipoDocumentoRepository tipoDocumentoRepository;

    @InjectMocks ClienteService service;

    private static final TipoDocumento TIPO_DOC = new TipoDocumento(1L, "RUT", "RUT chileno");
    private static final Cliente CLIENTE = new Cliente(
            1L, TIPO_DOC, "12345678-9", "Juan", "Perez", "juan@mail.com", "+56912345678");

    @Test
    void create_documentoNuevo_guardaYRetornaDTO() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "RUT", "12345678-9", "Juan", "Perez", "juan@mail.com", "+56912345678");

        when(clienteRepository.existsByNroDocumento("12345678-9")).thenReturn(false);
        when(tipoDocumentoRepository.findByCodigo("RUT")).thenReturn(Optional.of(TIPO_DOC));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(CLIENTE);

        ClienteResponseDTO result = service.create(dto);

        assertEquals(1L, result.getId());
        assertEquals("12345678-9", result.getNroDocumento());
        assertEquals("RUT", result.getCodigoTipoDocumento());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void create_documentoDuplicado_lanzaDuplicateResourceException() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "RUT", "12345678-9", "Juan", "Perez", null, null);

        when(clienteRepository.existsByNroDocumento("12345678-9")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.create(dto));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void create_tipoDocumentoNoExiste_lanzaEntityNotFoundException() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "PASS", "A12345678", "Juan", "Perez", null, null);

        when(clienteRepository.existsByNroDocumento("A12345678")).thenReturn(false);
        when(tipoDocumentoRepository.findByCodigo("PASS")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getByTipoYNumeroDocumento_encontrado_retornaDTO() {
        when(clienteRepository.findByTipoDocumentoCodigoAndNroDocumento("RUT", "12345678-9"))
                .thenReturn(Optional.of(CLIENTE));

        Optional<ClienteResponseDTO> result = service.getByTipoYNumeroDocumento("RUT", "12345678-9");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Juan", result.get().getNombre());
    }

    @Test
    void getByTipoYNumeroDocumento_noExiste_retornaVacio() {
        when(clienteRepository.findByTipoDocumentoCodigoAndNroDocumento("RUT", "99999999-9"))
                .thenReturn(Optional.empty());

        assertTrue(service.getByTipoYNumeroDocumento("RUT", "99999999-9").isEmpty());
    }

    @Test
    void getById_encontrado_retornaDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(CLIENTE));

        Optional<ClienteResponseDTO> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
        assertEquals("Perez", result.get().getApellido());
    }

    @Test
    void getById_noExiste_retornaVacio() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(service.getById(99L).isEmpty());
    }

    @Test
    void actualizarContacto_encontrado_actualizaYRetornaDTO() {
        Cliente clienteActual = new Cliente(1L, TIPO_DOC, "12345678-9", "Juan", "Perez", "old@mail.com", "111");
        ClienteContactoUpdateDTO dto = new ClienteContactoUpdateDTO("new@mail.com", "+56999999999");
        Cliente clienteActualizado = new Cliente(1L, TIPO_DOC, "12345678-9", "Juan", "Perez", "new@mail.com", "+56999999999");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteActual));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        Optional<ClienteResponseDTO> result = service.actualizarContacto(1L, dto);

        assertTrue(result.isPresent());
        assertEquals("new@mail.com", result.get().getEmail());
        assertEquals("+56999999999", result.get().getTelefono());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizarContacto_noExiste_retornaVacio() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ClienteResponseDTO> result = service.actualizarContacto(99L, new ClienteContactoUpdateDTO());

        assertTrue(result.isEmpty());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void listar_sinFiltro_usaFindAllPaginado() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(CLIENTE)));

        Page<ClienteResponseDTO> result = service.listar(null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(clienteRepository).findAll(pageable);
        verify(clienteRepository, never())
                .findByNombreContainingIgnoreCaseOrNroDocumentoContainingIgnoreCase(any(), any(), any());
    }

    @Test
    void listar_conFiltro_buscaPorNombreODocumento() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findByNombreContainingIgnoreCaseOrNroDocumentoContainingIgnoreCase(
                "Juan", "Juan", pageable)).thenReturn(new PageImpl<>(List.of(CLIENTE)));

        Page<ClienteResponseDTO> result = service.listar("Juan", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Juan", result.getContent().get(0).getNombre());
        verify(clienteRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void listar_filtroBlanco_usaFindAllPaginado() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(CLIENTE)));

        Page<ClienteResponseDTO> result = service.listar("   ", pageable);

        assertEquals(1, result.getTotalElements());
        verify(clienteRepository).findAll(pageable);
    }
}
