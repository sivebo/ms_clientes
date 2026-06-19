package com.sivebo.ms_clientes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sivebo.ms_clientes.dto.response.TipoDocumentoResponseDTO;
import com.sivebo.ms_clientes.repository.TipoDocumentoRepository;
import com.sivebo.ms_clientes.utils.MapToDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoDocumentoService extends MapToDTO {

        private final TipoDocumentoRepository tipoDocumentoRepository;

        public List<TipoDocumentoResponseDTO> getAll() {
                return tipoDocumentoRepository.findAll()
                                .stream()
                                .map(this::mapTipoDocumentoToDTO)
                                .collect(Collectors.toList());
        }

        public Optional<TipoDocumentoResponseDTO> getByCodigo(String codigo) {
                return tipoDocumentoRepository.findByCodigo(codigo).map(this::mapTipoDocumentoToDTO);
        }
}
