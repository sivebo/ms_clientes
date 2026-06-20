package com.sivebo.ms_clientes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivebo.ms_clientes.dto.response.TipoDocumentoResponseDTO;
import com.sivebo.ms_clientes.model.TipoDocumento;
import com.sivebo.ms_clientes.repository.TipoDocumentoRepository;

@ExtendWith(MockitoExtension.class)
class TipoDocumentoServiceTest {

    @Mock TipoDocumentoRepository tipoDocumentoRepository;

    @InjectMocks TipoDocumentoService service;

    private static final TipoDocumento RUT = new TipoDocumento(1L, "RUT", "RUT chileno");
    private static final TipoDocumento PASS = new TipoDocumento(2L, "PASS", "Pasaporte");

    @Test
    void getAll_retornaTodosLosTipos() {
        when(tipoDocumentoRepository.findAll()).thenReturn(List.of(RUT, PASS));

        List<TipoDocumentoResponseDTO> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("RUT", result.get(0).getCodigo());
        assertEquals("PASS", result.get(1).getCodigo());
    }

    @Test
    void getAll_sinRegistros_retornaListaVacia() {
        when(tipoDocumentoRepository.findAll()).thenReturn(List.of());

        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void getByCodigo_encontrado_retornaDTO() {
        when(tipoDocumentoRepository.findByCodigo("RUT")).thenReturn(Optional.of(RUT));

        Optional<TipoDocumentoResponseDTO> result = service.getByCodigo("RUT");

        assertTrue(result.isPresent());
        assertEquals("RUT", result.get().getCodigo());
        assertEquals("RUT chileno", result.get().getDescripcion());
    }

    @Test
    void getByCodigo_noExiste_retornaVacio() {
        when(tipoDocumentoRepository.findByCodigo("DNI")).thenReturn(Optional.empty());

        assertTrue(service.getByCodigo("DNI").isEmpty());
    }
}
