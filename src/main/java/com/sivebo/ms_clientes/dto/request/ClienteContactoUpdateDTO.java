package com.sivebo.ms_clientes.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteContactoUpdateDTO {

        @Email(message = "Email no válido")
        String email;

        String telefono;
}
