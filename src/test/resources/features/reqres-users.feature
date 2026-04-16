Feature: Gestion de usuarios en reqres.in Project API

  Como analista de automatizacion
  Quiero gestionar usuarios via la API de reqres.in
  Para validar operaciones CRUD con RestAssured y Screenplay

  Background:
    Given el analista puede consumir la API de reqres.in

  @listar
  Scenario: a. Listar usuarios y validar que la respuesta contenga al menos un usuario
    Given el servicio de usuarios esta disponible
    When el analista solicita la lista de usuarios
    Then la respuesta debe contener al menos un usuario
    And cada usuario debe tener email, first_name y last_name

  @registrar
  Scenario: b. Registrar un nuevo usuario y validar que la respuesta sea la esperada
    When el analista registra un usuario con first_name "Andreu" last_name "Sarmiento" y email "andreu.test@example.com"
    Then el codigo de respuesta debe ser 201
    And la respuesta debe contener un id generado
    And los datos del usuario deben coincidir con los enviados

  @registrar-error
  Scenario: c. Intentar registrar un usuario sin datos obligatorios y validar el error
    When el analista intenta registrar un usuario sin datos obligatorios
    Then el codigo de respuesta debe ser 400
    And la respuesta debe contener un mensaje de error

  @actualizar
  Scenario: d. Actualizar la informacion de un usuario existente y validar la actualizacion
    Given existe un usuario registrado en el sistema
    When el analista actualiza el usuario con first_name "Andreu Updated" last_name "Sarmiento Updated" y email "andreu.updated@example.com"
    Then el codigo de respuesta debe ser 200
    And los datos actualizados deben coincidir con los enviados
    And el id del usuario debe mantenerse igual

  @eliminar
  Scenario: e. Eliminar un usuario y validar que la operacion sea exitosa
    Given existe un usuario registrado en el sistema
    When el analista elimina el usuario
    Then el codigo de respuesta debe ser 204