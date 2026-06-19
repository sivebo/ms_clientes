package com.sivebo.ms_clientes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

        @NotBlank(message = "El código de tipo de documento es obligatorio")
        String codigoTipoDocumento;

        @NotBlank(message = "El número de documento es obligatorio")
        String nroDocumento;

        @NotBlank(message = "El nombre es obligatorio")
        String nombre;

        @NotBlank(message = "El apellido es obligatorio")
        String apellido;

        @Email(message = "Email no válido")
        String email;

        String telefono;
}
