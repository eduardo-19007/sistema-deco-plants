# Sistema DecoPlants

Sistema web de administración y venta de productos para DecoPlants.

## Descripción

Aplicación Java Spring Boot con front-end Thymeleaf y backend REST para manejar:
- Catálogo de productos
- Gestión de órdenes y pedidos
- Gestión de incidencias / reclamos postventa
- Panel administrativo para productos, pedidos e incidencias
- Autenticación con Spring Security y JWT para la API
- Persistencia con Spring Data JPA y Microsoft SQL Server

## Tecnologías

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA
- Spring Security
- Thymeleaf
- JWT (`io.jsonwebtoken`)
- Microsoft SQL Server (`mssql-jdbc`)
- Lombok

## Estructura del proyecto

- `src/main/java/com/decoplants/sistema_web/` - código fuente
  - `config/` - configuración de seguridad, JWT y MVC
  - `controllers/` - controladores web y API
  - `models/` - entidades JPA
  - `repositories/` - repositorios Spring Data
  - `services/` - lógica de negocio
  - `dtos/` - objetos de transferencia de datos
  - `exceptions/` - manejo de errores global
- `src/main/resources/templates/` - vistas Thymeleaf
- `src/main/resources/application.properties` - configuración de la aplicación
- `uploads/` - carpeta para almacenar imágenes de productos subidas por el usuario

## Requisitos

- Java 21
- Maven (o usar `./mvnw` / `mvnw.cmd`)
- SQL Server con una base de datos configurada

## Configuración

La aplicación usa `src/main/resources/application.properties` para la conexión a SQL Server:

```properties
spring.datasource.url=jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=DecoPlantsDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=123456
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.thymeleaf.cache=false
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB
```

> Ajusta la URL, usuario y contraseña según tu entorno de SQL Server antes de ejecutar la aplicación.

## Ejecución

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

o con Maven instalado:

```bash
mvn spring-boot:run
```

También puedes construir el JAR y ejecutarlo:

```bash
./mvnw package
java -jar target/sistema-web-0.0.1-SNAPSHOT.jar
```

## Rutas principales

### Web

- `/login` - página de inicio de sesión
- `/usuario` - vista de usuario
- `/admin/productos` - panel de administración de productos
- `/admin/pedidos` - panel de administración de pedidos
- `/admin/incidencias` - panel de administración de incidencias
- `/registrar-pedido` - formulario para crear pedidos
- `/registrar-incidencia` - formulario para registrar una incidencia

### API REST

- `POST /api/auth/login` - login y generación de JWT
- `GET /api/productos` - listado paginado de productos activos
- `GET /api/productos/{id}` - obtener producto por ID
- `POST /api/productos` - crear producto (requiere rol ADMIN)
- `PUT /api/productos/{id}` - actualizar producto (requiere rol ADMIN)
- `DELETE /api/productos/{id}` - eliminar producto (requiere rol ADMIN)

## Modelo de datos

Entidades principales:

- `Producto` - nombre, descripción, precio, stock, imagen, estado, categoría
- `Categoria` - nombre, descripción, estado
- `Pedido` - datos del cliente, método de pago, modalidad de entrega, total, estado y detalles
- `DetallePedido` - relación entre pedidos y productos, cantidad, precio unitario y subtotal
- `Incidencia` - datos de cliente, tipo de reclamo, descripción, fecha de registro y estado
- `Usuario` - credenciales, nombre, rol y estado

## Seguridad

- Form login para la interfaz web
- JWT para la API REST
- Roles usados en seguridad: `ADMIN`, `VENDEDOR`, y cliente estándar

## Notas importantes

- La aplicación usa `spring.jpa.hibernate.ddl-auto=update`, por lo que las tablas se crearán/actualizarán automáticamente.
- Asegúrate de que la carpeta `uploads/` exista y tenga permisos de escritura para las imágenes de productos.
- El token JWT se firma con una clave interna generada en `JwtUtil`; en un entorno real conviene externalizar la clave secreta.

## Contribuir

1. Clona el repositorio
2. Configura tu SQL Server y actualiza `application.properties`
3. Ejecuta `./mvnw spring-boot:run`
4. Accede a `/login` o usa la API con `/api/auth/login`

---

Este `README.md` documenta la instalación, las rutas y las capacidades principales del proyecto DecoPlants.