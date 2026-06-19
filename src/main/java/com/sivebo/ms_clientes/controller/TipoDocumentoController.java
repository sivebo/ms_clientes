package com.sivebo.ms_clientes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_clientes.dto.response.TipoDocumentoResponseDTO;
import com.sivebo.ms_clientes.service.TipoDocumentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/tipos-documento")
@RequiredArgsConstructor
@Tag(name = "Tipos de Documento", description = "Catálogo de tipos de documento (RUT, Pasaporte, DNI, etc.)")
public class TipoDocumentoController {

        private final TipoDocumentoService tipoDocumentoService;

        @Operation(summary = "Listar todos los tipos de documento")
        @GetMapping
        public List<TipoDocumentoResponseDTO> getAll() {
                return tipoDocumentoService.getAll();
        }

        @Operation(summary = "Obtener tipo de documento por código")
        @GetMapping("/{codigo}")
        public ResponseEntity<TipoDocumentoResponseDTO> getByCodigo(@PathVariable String codigo) {
                return tipoDocumentoService.getByCodigo(codigo)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }
}
