# Reto Tecnico - Automatizacion de API con RestAssured, Serenity BDD y Screenplay

Proyecto de automatizacion de pruebas para una coleccion personalizada de usuarios en la plataforma **reqres.in**, utilizando **RestAssured**, **Serenity BDD**, patron **Screenplay** y **Cucumber**.

Este proyecto fue creado a partir del template oficial [serenity-cucumber-starter](https://github.com/serenity-bdd/serenity-cucumber-starter), adaptado para pruebas de API REST reemplazando los escenarios web originales por escenarios de servicios.

## API bajo prueba

Este proyecto consume una coleccion propia configurada en reqres.in, no los endpoints publicos por defecto. La coleccion `users` utiliza los siguientes campos personalizados:

| Campo        | Tipo   | Descripcion           |
|--------------|--------|-----------------------|
| `email`      | String | Correo del usuario    |
| `first_name` | String | Nombre del usuario    |
| `last_name`  | String | Apellido del usuario  |

### Endpoints utilizados

| Operacion | Metodo | Endpoint                              |
|-----------|--------|---------------------------------------|
| Listar    | GET    | `/api/collections/users/records`      |
| Crear     | POST   | `/api/collections/users/records`      |
| Actualizar| PUT    | `/api/collections/users/records/{id}` |
| Eliminar  | DELETE | `/api/collections/users/records/{id}` |

Todas las peticiones requieren el header `x-api-key` para autenticacion.

## Estructura del proyecto

```
src/test/java/api
├── models/
│   └── UserData.java              # Modelo de datos del usuario
├── tasks/
│   ├── ListUsers.java             # listar usuarios - GET
│   ├── CreateUser.java            # crear usuario - POST
│   ├── UpdateUser.java            # actualizar usuario - PUT
│   └── DeleteUser.java            # eliminar usuario - DELETE
├── questions/
│   ├── ResponseCode.java          # codigo de respuesta HTTP
│   ├── UserListResponse.java      # lista de usuarios 
│   ├── UserFieldResponse.java     # campos del usuario 
│   └── ErrorResponse.java         # mensaje de error 
├── stepdefinitions/
│   └── UsersStepDefinitions.java  # Definiciones de pasos Cucumber
└── runners/
    └── CucumberTestSuite.java     # Runner JUnit 5

src/test/resources
├── features/
│   └── reqres-users.feature       # Escenarios Gherkin
├── serenity.conf                  # Configuracion de Serenity - base URL, API key
└── junit-platform.properties      # Configuracion de Cucumber y JUnit Platform
```

## Patron Screenplay

El proyecto sigue el patron Screenplay de Serenity BDD:

- Tasks: representan las acciones del actor (GET, POST, PUT, DELETE) usando las interacciones REST de Serenity (`Get`, `Post`, `Put`, `Delete`).
- Questions: extraen informacion de la respuesta usando `SerenityRest.lastResponse()` y `JsonPath`.
- Actor: el analista que ejecuta las tareas y valida los resultados con `seeThat`.
- Modelo: la respuesta se guarda en un objeto `UserData` mediante `actor.remember()` en el step When, y se recupera con `actor.recall()` en el step Then para realizar las validaciones.

## Escenarios de prueba

| Tag               | Escenario                                                  |
|--------------------|------------------------------------------------------------|
| `@listar`          | Listar usuarios y validar que la respuesta contenga datos  |
| `@registrar`       | Registrar un usuario y validar la respuesta                |
| `@registrar-error` | Registrar sin datos obligatorios y validar el error        |
| `@actualizar`      | Actualizar un usuario y validar la actualizacion           |
| `@eliminar`        | Eliminar un usuario y validar la operacion exitosa         |

## Requisitos previos

- Java 17 o superior
- Gradle 8+ (se incluye Gradle Wrapper)

## Ejecucion de pruebas

Ejecutar todas las pruebas y generar el reporte:

```bash
./gradlew clean test aggregate
```

Ejecutar un escenario especifico por tag:

```bash
./gradlew clean test aggregate -Dcucumber.filter.tags="@listar"
```

## Reportes

Despues de la ejecucion, los reportes de Serenity BDD se generan automaticamente en:

- Reporte completo: `target/site/serenity/index.html`
- Resumen HTML: `target/site/serenity/serenity-summary.html`

Los reportes incluyen detalle de cada llamada REST realizada, headers, body enviado, respuesta recibida y codigo HTTP.

## Configuracion

La URL base y la API key se configuran en `src/test/resources/serenity.conf`:

```hocon
restapi {
  baseurl = "https://reqres.in"
}

reqres {
  api {
    key = "api-key-de-reqres"
  }
}
```

## Entregables

- Codigo fuente del proyecto
- Reportes generados por Serenity BDD
- Coleccion de Postman archivo `.json` adjunto en `.zip`
