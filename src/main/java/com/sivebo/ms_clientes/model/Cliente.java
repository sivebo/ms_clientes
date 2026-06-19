package com.sivebo.ms_clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @ManyToOne
        @JoinColumn(name = "id_tipo_doc", nullable = false)
        TipoDocumento tipoDocumento;

        @Column(name = "nro_documento", nullable = false, unique = true, length = 20)
        String nroDocumento;

        @Column(name = "nombre", nullable = false, length = 100)
        String nombre;

        @Column(name = "apellido", nullable = false, length = 100)
        String apellido;

        @Column(name = "email", length = 100)
        String email;

        @Column(name = "telefono", length = 20)
        String telefono;
}
