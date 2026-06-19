package com.sivebo.ms_clientes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoResponseDTO {

        Long id;
        String codigo;
        String descripcion;
}
