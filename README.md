# ms_clientes

Microservicio de gestión de clientes del sistema **SIVEBO** (Sistema de Gestión de Envíos y Bodega).

---

## Descripción

Gestiona el CRUD completo de clientes. Permite crear, consultar, actualizar y eliminar clientes, además de búsqueda por nombre. Los clientes registrados aquí son referenciados por `ms_admision` al momento de ingresar paquetes.

---

## Tecnologías

- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- MariaDB
- Lombok
- Maven

---

## Base de datos

```
db_ms_clientes
```

---

## Configuración

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/db_ms_clientes?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
server.port=8085
```

---

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/clientes` | Crea un nuevo cliente |
| GET | `/clientes` | Lista todos los clientes |
| GET | `/clientes?nombre=Juan` | Busca clientes por nombre (parcial) |
| GET | `/clientes/{id}` | Obtiene un cliente por ID |
| PUT | `/clientes/{id}` | Actualiza los datos de un cliente |
| DELETE | `/clientes/{id}` | Elimina un cliente |

### Ejemplo — Crear cliente

```json
POST /clientes
{
  "nombre": "Juan Pérez",
  "rut": "12345678-9",
  "telefono": "912345678",
  "email": "juan.perez@email.cl"
}
```

### Respuesta

```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "rut": "12345678-9",
  "telefono": "912345678",
  "email": "juan.perez@email.cl"
}
```

---

## Estructura del proyecto

```
src/main/java/com/sivebo/ms_clientes/
├── config/
│   └── GlobalExceptionHandler.java
├── controller/
│   └── ClienteController.java
├── dto/
│   ├── ClienteRequest.java
│   └── ClienteResponse.java
├── model/
│   └── Cliente.java
├── repository/
│   └── ClienteRepository.java
└── service/
    └── ClienteService.java
```

---

## Ejecución

```bash
./mvnw spring-boot:run
```

---

## Notas

- El campo `rut` es único por cliente y debe tener el formato chileno estándar (ej: `12345678-9`).
- El campo `email` también es único. Ambos se validan al crear y al actualizar.
- La búsqueda por nombre (`?nombre=`) es parcial y no distingue mayúsculas de minúsculas.
- La base de datos se crea automáticamente si no existe.
