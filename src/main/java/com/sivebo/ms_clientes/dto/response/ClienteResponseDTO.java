package com.sivebo.ms_clientes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

        Long id;
        String codigoTipoDocumento;
        String nroDocumento;
        String nombre;
        String apellido;
        String email;
        String telefono;
}
