package com.sivebo.ms_clientes.utils;

import com.sivebo.ms_clientes.dto.response.ClienteResponseDTO;
import com.sivebo.ms_clientes.dto.response.TipoDocumentoResponseDTO;
import com.sivebo.ms_clientes.model.Cliente;
import com.sivebo.ms_clientes.model.TipoDocumento;

public class MapToDTO {

        protected ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
                return new ClienteResponseDTO(
                                cliente.getId(),
                                cliente.getTipoDocumento().getCodigo(),
                                cliente.getNroDocumento(),
                                cliente.getNombre(),
                                cliente.getApellido(),
                                cliente.getEmail(),
                                cliente.getTelefono());
        }

        protected TipoDocumentoResponseDTO mapTipoDocumentoToDTO(TipoDocumento tipoDocumento) {
                return new TipoDocumentoResponseDTO(
                                tipoDocumento.getId(),
                                tipoDocumento.getCodigo(),
                                tipoDocumento.getDescripcion());
        }
}
