package com.sivebo.ms_clientes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sivebo.ms_clientes.model.TipoDocumento;
import com.sivebo.ms_clientes.repository.TipoDocumentoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final TipoDocumentoRepository tipoDocumentoRepository;

        @Override
        public void run(String... args) {
                if (tipoDocumentoRepository.count() > 0) {
                        log.info(">>> Tipos de documento ya cargados. Se omite inicialización.");
                        return;
                }

                log.info(">>> Cargando tipos de documento iniciales...");

                tipoDocumentoRepository.save(new TipoDocumento(null, "RUT", "Rol Único Tributario (Chile)"));
                tipoDocumentoRepository.save(new TipoDocumento(null, "PASAPORTE", "Pasaporte internacional"));
                tipoDocumentoRepository.save(new TipoDocumento(null, "DNI", "Documento Nacional de Identidad"));

                log.info(">>> Tipos de documento iniciales cargados exitosamente.");
        }
}
