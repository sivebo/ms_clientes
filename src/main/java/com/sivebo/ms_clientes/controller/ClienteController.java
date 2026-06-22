package com.sivebo.ms_clientes.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_clientes.dto.request.ClienteContactoUpdateDTO;
import com.sivebo.ms_clientes.dto.request.ClienteRequestDTO;
import com.sivebo.ms_clientes.dto.response.ClienteResponseDTO;
import com.sivebo.ms_clientes.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes (remitentes y destinatarios)")
public class ClienteController {

        private final ClienteService clienteService;

        @Operation(summary = "Registrar un nuevo cliente", description = "crea un cliente remitente o destinatario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class)))
        })
        @PostMapping
        public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO dto) {
                log.info(">>> POST /api/v1/clientes - nroDocumento: {}", dto.getNroDocumento());
                return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(dto));
        }

        @Operation(summary = "Buscar cliente por tipo y número de documento", description = "búsqueda exacta por tipoDocumento + nroDocumento")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
        })
        @GetMapping("/buscar")
        public ResponseEntity<ClienteResponseDTO> buscarPorDocumento(
                        @RequestParam String tipoDocumento,
                        @RequestParam String nroDocumento) {
                return clienteService.getByTipoYNumeroDocumento(tipoDocumento, nroDocumento)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Obtener cliente por id")
        @GetMapping("/{id}")
        public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id) {
                return clienteService.getById(id)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Listar clientes paginados", description = "lista con paginación y filtro opcional por nombre o número de documento. Ej: ?filtro=Juan&page=0&size=10")
        @GetMapping
        public ResponseEntity<Page<ClienteResponseDTO>> listar(
                        @RequestParam(required = false) String filtro,
                        Pageable pageable) {
                return ResponseEntity.ok(clienteService.listar(filtro, pageable));
        }

        @Operation(summary = "Actualizar datos de contacto", description = "actualiza solo email y teléfono del cliente")
        @PutMapping("/{id}/contacto")
        public ResponseEntity<ClienteResponseDTO> actualizarContacto(
                        @PathVariable Long id,
                        @Valid @RequestBody ClienteContactoUpdateDTO dto) {
                return clienteService.actualizarContacto(id, dto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }
}
