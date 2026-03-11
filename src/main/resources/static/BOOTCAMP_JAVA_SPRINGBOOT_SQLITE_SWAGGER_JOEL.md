# Bootcamp (12h) — Java + Spring Boot + SQLite + Swagger (OpenAPI)

### Chuleta 
- Infra - Todo lo que tiene que ver cn acceso a datos
- Domain - Service y models
- App - Config y controllers
---
**Objetivo**: que una persona junior cree una API REST con Spring Boot, persistencia con **SQLite** usando **Spring Data JPA**, y documentación/ejecución desde **Swagger UI**.

**Resultado final (al terminar 12h)**:
- API CRUD (tema propuesto: **Biblioteca / Préstamos de Libros** con relaciones JPA).
- Base de datos **SQLite** en archivo local (persistencia real).
- Endpoints visibles y probables desde **Swagger UI**.
- Estructura de proyecto escalable para introducir conceptos más avanzados.

---

## Requisitos previos (5–10 min)
- Java 17+ (o el que use el proyecto).
- Maven (o `./mvnw`).
- IDE (VS Code / IntelliJ).
- Conocimientos básicos: clases, getters/setters, colecciones, HTTP básico.

> Nota: este repositorio ya tiene un proyecto Spring Boot creado. Vamos a **añadir** un nuevo módulo/feature (ej. `library`/`loans`) sin romper lo existente.

---

## Agenda (tiempos realistas para perfil junior)

Con 4 entidades (`Book`, `Member`, `Loan`, `Category`), relaciones **1-N** y **N-N**, validaciones, errores consistentes y Swagger, el tiempo realista para una persona junior suele ser **10–12 horas** (dependiendo de cuánto “en serio” se haga el mapping/DTOs y la gestión de errores).

A continuación va una planificación realista de **~11 horas** (recomendada).  
Si necesitas que encaje estrictamente en 8h, al final hay una propuesta “recortada”.

### Plan recomendado (~11 horas)
1. **(0:00–0:40)** Preparación del entorno, arranque del proyecto y revisión de dependencias
2. **(0:40–1:40)** Diseño del dominio y contratos de la API (Books/Members/Loans/Categories)
3. **(1:40–3:30)** Persistencia con SQLite + JPA (entidades + mapeos + repos)
4. **(3:30–6:00)** Implementación de endpoints CRUD (Books, Members, Categories) + filtros básicos
5. **(6:00–7:30)** Loans (caso de uso con relaciones): crear préstamo + devolver + filtros `active/memberId/bookId`
6. **(7:30–8:30)** Swagger/OpenAPI + ejemplos + revisión de esquemas (evitar recursividad)
7. **(8:30–9:45)** Validaciones + manejo global de errores (400/404/409) + respuestas consistentes
8. **(9:45–10:45)** Pruebas mínimas (1–2 unit tests + 1 integración de flujo)
9. **(10:45–11:00)** Refactor final + checklist + puntos de escalado

---

# 0) Preparación y arranque (0:00–0:30)

## 0.1 — Ejecutar el proyecto
1. En terminal:
   - `mvnw.cmd spring-boot:run` (Windows)
   - o `./mvnw spring-boot:run` (Mac/Linux)
2. Verifica que arranca sin errores.
3. Localiza el puerto (normalmente `8080`).

## 0.2 — Confirmar dependencias existentes
- Abre `pom.xml` y busca:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - (posible) `springdoc-openapi` o similar

> Si falta algo, lo añadiremos más tarde (en un “bonus” o en el momento que toque).

## 0.3 — Objetivo de feature
Crearemos un módulo **library** (o `loans`) con varias entidades y relaciones para practicar mapeos JPA:

### Entidades (mínimas)
- `Book`
  - `id`, `isbn` (único), `title`, `author`, `publishedYear`
- `Member`
  - `id`, `fullName`, `email` (único), `joinedAt`
- `Loan`
  - `id`, `loanDate`, `dueDate`, `returnDate` (nullable)
- `Category`
  - `id`, `name` (único)

### Relaciones JPA a practicar
- `Member (1) --- (N) Loan`  → `@OneToMany` / `@ManyToOne`
- `Book (1) --- (N) Loan`    → `@OneToMany` / `@ManyToOne`
- `Book (N) --- (N) Category` → `@ManyToMany` con tabla intermedia (ej. `book_categories`)

> El CRUD principal será sobre `Books` y `Members`, y el caso de uso “interesante” será crear/gestionar `Loans` (préstamos), que fuerza a entender bien los mapeos y las FK.

---

# 1) Diseño del dominio y la API (0:30–1:30)

## 1.1 — Definir endpoints (contrato)
Vamos a exponer 4 recursos principales: **Books**, **Members**, **Loans** y **Categories**.

### Books (CRUD)
- `POST /api/books` → crear libro
- `GET /api/books` → listar libros (filtros opcionales)
- `GET /api/books/{id}` → detalle
- `PUT /api/books/{id}` → actualizar
- `DELETE /api/books/{id}` → eliminar

Filtros “fáciles”:
- `GET /api/books?title=harry` (contains)
- `GET /api/books?author=rowling`
- (Opcional) `GET /api/books?isbn=...`

### Members (CRUD)
- `POST /api/members` → alta socio
- `GET /api/members` → listar socios
- `GET /api/members/{id}` → detalle
- `PUT /api/members/{id}` → actualizar
- `DELETE /api/members/{id}` → baja

Filtros “fáciles”:
- `GET /api/members?email=...`
- `GET /api/members?name=ana` (contains)

### Loans (casos de uso con relaciones)
- `POST /api/loans` → crear préstamo (relaciona `memberId` + `bookId`)
- `GET /api/loans` → listar préstamos (por `memberId`, `bookId`, “activos”, etc.)
- `GET /api/loans/{id}` → detalle
- `POST /api/loans/{id}/return` → marcar devolución (set `returnDate`)
- (Opcional) `DELETE /api/loans/{id}` → borrar (para enseñar restricciones/integridad)

Filtros recomendados:
- `GET /api/loans?memberId=1`
- `GET /api/loans?bookId=10`
- `GET /api/loans?active=true` (returnDate = null)

### Categories (CRUD)
- `POST /api/categories` → crear categoría
- `GET /api/categories` → listar categorías
- `GET /api/categories/{id}` → detalle
- `PUT /api/categories/{id}` → actualizar
- `DELETE /api/categories/{id}` → eliminar

Filtros “fáciles”:
- `GET /api/categories?name=ficcion` (contains)

## 1.2 — DTOs (entrada/salida)
Books:

Members:

Loans:

Categories:

Reglas:
- Request ≠ Entity (evitar exponer entidades JPA directamente).
- Response con campos controlados y sin bucles infinitos (muy importante con relaciones).

## 1.3 — Estructura por capas (sugerida)
- `library/domain/model`
- `library/domain/port`
- `library/domain/usecase` (o `service`)
- `library/application/controller`
- `library/infrastructure/persistence/jpa/entity`
- `library/infrastructure/persistence/jpa/repository`

> Objetivo didáctico: practicar mapeos JPA sin “acoplar” el dominio a la base de datos y evitar problemas típicos (N+1, serialización recursiva).

---

# 2) Persistencia con SQLite + JPA (1:30–3:00)

## 2.1 — Añadir dependencias para SQLite
En `pom.xml` (si no existen):
- Driver SQLite:
  - `org.xerial:sqlite-jdbc`
- Dialecto Hibernate para SQLite (opciones):
  - Opción A (recomendada): `org.hibernate.orm:hibernate-community-dialects`
  - Opción B: librería de terceros `com.github.gwenn:sqlite-dialect` (si procede)

> Explicar: Hibernate necesita saber cómo hablar con la BD (dialecto). SQLite no siempre viene “de serie”.

## 2.2 — Configurar `application.properties`
En `src/main/resources/application.properties` añadir:
- `spring.datasource.url=jdbc:sqlite:./data/demo.db` (por ejemplo)
- `spring.datasource.driver-class-name=org.sqlite.JDBC`
- Dialecto hibernate:
  - `spring.jpa.database-platform=...`
- Para el bootcamp:
  - `spring.jpa.hibernate.ddl-auto=update` (más cómodo)
  - luego explicar por qué en prod usar `validate` + migraciones.

> **Importante**: actualmente el proyecto tiene:
> - `spring.jpa.hibernate.ddl-auto=validate`
> Para el ejercicio, cambiarlo a `update` o `create-drop` (si se quiere empezar limpio).

## 2.3 — Crear entidades JPA (relaciones)
Crear entidades con FK reales (y discutir `FetchType`, `cascade`, `orphanRemoval`).

### `BookEntity`
- `@Entity`, `@Table(name = "books")`

### `MemberEntity`
- `@Entity`, `@Table(name = "members")`

### `LoanEntity`
- `@Entity`, `@Table(name = "loans")`

> Nota didáctica: empezar **sin** `@OneToMany` en `BookEntity/MemberEntity` para simplificar (relación unidireccional desde `LoanEntity`). Luego se añade la bidireccionalidad para enseñar `mappedBy` y problemas de serialización.

## 2.4 — Repositorios Spring Data
- `SpringDataBookRepository extends JpaRepository<BookEntity, Long>`

- `SpringDataMemberRepository extends JpaRepository<MemberEntity, Long>`

- `SpringDataLoanRepository extends JpaRepository<LoanEntity, Long>`

- `SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long>`

> Punto didáctico: queries derivadas vs `@Query` cuando el método se complica.

---

# 3) Implementación de casos de uso y mapping (3:00–4:30)

## 3.1 — Modelo de dominio
Modelos sugeridos (dominio):
- `Book`, `Member`, `Loan`
- Mantener el dominio libre de anotaciones JPA si es posible.

Reglas de negocio para hacerlo interesante:
- No permitir 2 préstamos activos del mismo libro (`returnDate` null) si quieres simular “stock 1”.
- `dueDate` debe ser posterior a `loanDate`.
- Solo se puede devolver un préstamo si está activo.

## 3.2 — Puertos (interfaces) de repositorio
- `BookRepositoryPort`
- `MemberRepositoryPort`
- `LoanRepositoryPort`
- `CategoryRepositoryPort`

Operaciones mínimas:
- `save(...)`, `findById(...)`, `findAll(...)`, `deleteById(...)`
- y métodos específicos: “find active loans”, “find by member”, “find by book”.

## 3.3 — Adaptadores infraestructura
- `JpaBookRepositoryAdapter` (BookEntity ⇄ Book)
- `JpaMemberRepositoryAdapter` (MemberEntity ⇄ Member)
- `JpaLoanRepositoryAdapter` (LoanEntity ⇄ Loan)
- `JpaCategoryRepositoryAdapter` (CategoryEntity ⇄ Category)

> Punto didáctico importante: mapear relaciones (Loan contiene Book/Member o solo ids).  
> Empezar simple: `Loan` en dominio con `bookId`/`memberId` y luego evolucionar a objetos anidados.

## 3.4 — Servicios / Use cases
Books:

Members:

Categories:

Loans (donde se practica relación):

Excepciones:

---

# 4) Controlador REST (4:30–5:30)

## 4.1 — Controladores REST
Crear controladores separados (mejor para escalar) o uno por módulo:

### `BookController`
- `@RequestMapping("/api/books")`

### `MemberController`
- `@RequestMapping("/api/members")`

### `LoanController`
- `@RequestMapping("/api/loans")`

### `CategoryController`
- `@RequestMapping("/api/categories")`

## 4.2 — Códigos HTTP recomendados
- POST create → `201 Created`
- GET → `200 OK`
- PUT → `200 OK` (o `204 No Content`)
- DELETE → `204 No Content`
- Not found → `404`
- Reglas de negocio (ej. “libro ya prestado”) → `409 Conflict`

---

# 5) Swagger / OpenAPI (5:30–6:30)

## 5.1 — Añadir Springdoc (si no existe)
Dependencia:
- `org.springdoc:springdoc-openapi-starter-webmvc-ui`

## 5.2 — Ver Swagger UI
Rutas típicas:
- `http://localhost:8080/swagger-ui.html`
- o `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON:
  - `http://localhost:8080/v3/api-docs`

## 5.3 — Documentar el API
Añadir anotaciones (mínimas, para no saturar):
- `@Operation(summary=..., description=...)`
- `@ApiResponses(...)`
- `@Schema` en DTOs

Añadir ejemplos:
- Example de `LoanCreateRequest` con `memberId`, `bookId`, `dueDate`.
- Example de `BookCreateRequest` con `isbn`, `title`, `author`.
- Example de `MemberCreateRequest` con `fullName`, `email`.
- Example de `CategoryCreateRequest` con `name`.

> Punto didáctico: si devuelves objetos anidados (LoanResponse incluye BookResponse/MemberResponse), revisa el esquema OpenAPI y evita recursividad.

---

# 6) Validación y manejo de errores (6:30–7:30)

## 6.1 — Bean Validation
Añadir `spring-boot-starter-validation` si falta.

En DTOs (ejemplos):
- Books:
  - `@NotBlank` en `isbn`, `title`, `author`
  - `@Size(max = ...)` en `title`, `author`
- Members:
  - `@NotBlank` en `fullName`
  - `@Email` + `@NotBlank` en `email`
- Loans:
  - `@NotNull` en `memberId`, `bookId`, `dueDate`

> Punto didáctico: además de validación “de forma”, hay validación “de negocio” (existencia de Book/Member, libro ya prestado, etc.) que vive en los use cases.

## 6.2 — Manejo global de errores
Crear `@RestControllerAdvice`:
- Capturar `MethodArgumentNotValidException` → devolver 400 con lista de errores
- Capturar `BookNotFoundException`, `MemberNotFoundException`, `LoanNotFoundException` → 404
- Capturar `BookAlreadyLoanedException` → 409
- Capturar `Exception` genérica → 500 con `traceId` (opcional)

> Punto didáctico: contrato consistente de errores para frontend.

---

# 7) Pruebas mínimas (7:30–7:50)

## 7.1 — Unit test del servicio
- Probar `CreateLoanUseCase` con mocks de repos:
  - si Book no existe → 404 (o excepción de dominio)
  - si Member no existe → 404
  - si el libro ya tiene un préstamo activo → 409
- Probar `ReturnLoanUseCase`:
  - si ya está devuelto → error de negocio (409 o 400 según criterio)

## 7.2 — Integración del controlador (opcional)
- `@SpringBootTest` + `@AutoConfigureMockMvc`
- Probar flujo mínimo:
  1) `POST /api/books` → 201
  2) `POST /api/members` → 201
  3) `POST /api/loans` → 201
  4) `POST /api/loans/{id}/return` → 200/204

---

# 8) Cierre y checklist (7:50–8:00)

## Checklist final (debe cumplirse)
- [ ] La app arranca.
- [ ] Se crea el archivo SQLite (ej. `data/demo.db`).
- [ ] Se crean tablas automáticamente (ddl-auto=update durante el bootcamp).
- [ ] `POST /api/books` crea un libro.
- [ ] `POST /api/members` crea un socio.
- [ ] `POST /api/categories` crea una categoría.
- [ ] Asignar categorías a un libro (vía Many-to-Many, en creación o actualización).
- [ ] `POST /api/loans` crea un préstamo (con FK a book + member).
- [ ] `POST /api/loans/{id}/return` marca la devolución.
- [ ] `GET /api/loans?active=true` lista solo préstamos activos.
- [ ] Swagger UI muestra todos los endpoints y los DTOs (sin bucles).
- [ ] Validación de requests devuelve 400 con mensajes.
- [ ] “No encontrado” devuelve 404.
- [ ] Regla de negocio (ej. “libro ya prestado”) devuelve 409.

## Qué explicar al final (5 min)
- Diferencia entre:
  - Entidad JPA vs Modelo de dominio vs DTO.
- Por qué SQLite sirve para demo/POC.
- Qué cambia al ir a PostgreSQL/MySQL.
- Qué es OpenAPI y por qué Swagger acelera el desarrollo.

---

# Extras (para escalar progresivamente)
Estas mejoras están pensadas para iteraciones posteriores (otro día / más horas):

## A) Paginación y ordenación
- `GET /api/books?page=0&size=20&sort=title,asc`
- `GET /api/loans?page=0&size=20&sort=loanDate,desc`

## B) Migraciones con Flyway o Liquibase
- Cambiar `ddl-auto` a `validate`.
- Crear scripts de migración versionados.

## C) Seguridad con Spring Security + JWT
- Registro/login.
- Proteger endpoints.
- Roles: `ADMIN`, `USER`.

## D) Observabilidad
- Actuator (`/actuator/health`, métricas).
- Logging estructurado.

## E) Arquitectura
- Separar “application service” vs “domain service”.
- Introducir eventos de dominio (cuando un préstamo se crea o se devuelve).
- Introducir `@Transactional` (dónde y por qué) en use cases de préstamos.

## F) Documentación avanzada en OpenAPI
- `@SecurityRequirement`
- `@Parameter` con ejemplos y constraints
- “Problem Details” RFC7807 para errores.

## G) Docker
- Empaquetar app.
- Persistir SQLite en volumen.
- O migrar a Postgres con docker-compose.

---

# Notas para el instructor (para refinar)
- Decide si se seguirá “hexagonal ligera” como en `users/` o un enfoque más simple.
- Decide si `PATCH` se incluye o no (para junior puede ser opcional).
- Decide el nivel de OpenAPI: mínimo (solo swagger) vs anotaciones extensas.
- Decide si se usan `records` para DTOs (Java 16+) o clases normales.
