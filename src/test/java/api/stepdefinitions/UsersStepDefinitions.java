package api.stepdefinitions;

import api.models.UserData;
import api.questions.*;
import api.tasks.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class UsersStepDefinitions {

    private Actor analista;
    private String apiKey;
    private String recordId;

    @Before
    public void setup() {
        EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = env.optionalProperty("restapi.baseurl").orElse("https://reqres.in");
        apiKey = env.optionalProperty("reqres.api.key").orElse("");

        analista = Actor.named("Analista");
        analista.whoCan(CallAnApi.at(baseUrl));
    }

    @Given("el analista puede consumir la API de reqres.in")
    public void elAnalistaPuedeConsumirLaApi() {
    }

    @Given("el servicio de usuarios esta disponible")
    public void elServicioDeUsuariosEstaDisponible() {
        analista.attemptsTo(ListUsers.withApiKey(apiKey));
        analista.should(
                seeThat(ResponseCode.value(), equalTo(200)),
                seeThat(UserListResponse.users(), is(not(empty())))
        );
    }

    @Given("existe un usuario registrado en el sistema")
    public void existeUnUsuarioRegistrado() {
        analista.attemptsTo(
                CreateUser.withData(apiKey, "precondition@test.com", "Pre", "Condition")
        );
        analista.should(
                seeThat(ResponseCode.value(), equalTo(201))
        );
        recordId = analista.asksFor(UserFieldResponse.recordId());
    }

    @When("el analista solicita la lista de usuarios")
    public void elAnalistaSolicitaLaListaDeUsuarios() {
        analista.attemptsTo(ListUsers.withApiKey(apiKey));
        analista.remember("responseBody", SerenityRest.lastResponse().asString());
    }

    @When("el analista registra un usuario con first_name {string} last_name {string} y email {string}")
    public void elAnalistaRegistraUnUsuario(String firstName, String lastName, String email) {
        analista.attemptsTo(
                CreateUser.withData(apiKey, email, firstName, lastName)
        );

        UserData responseModel = new UserData(
                SerenityRest.lastResponse().jsonPath().getString("data.data.email"),
                SerenityRest.lastResponse().jsonPath().getString("data.data.first_name"),
                SerenityRest.lastResponse().jsonPath().getString("data.data.last_name")
        );
        analista.remember("userResponse", responseModel);
        analista.remember("sentData", new UserData(email, firstName, lastName));

        if (analista.asksFor(ResponseCode.value()) == 201) {
            recordId = analista.asksFor(UserFieldResponse.recordId());
        }
    }

    @When("el analista intenta registrar un usuario sin datos obligatorios")
    public void elAnalistaIntentaRegistrarSinDatos() {
        analista.attemptsTo(CreateUser.withEmptyData(apiKey));
        analista.remember("responseBody", SerenityRest.lastResponse().asString());
    }

    @When("el analista actualiza el usuario con first_name {string} last_name {string} y email {string}")
    public void elAnalistaActualizaElUsuario(String firstName, String lastName, String email) {
        analista.attemptsTo(
                UpdateUser.withData(apiKey, recordId, email, firstName, lastName)
        );

        UserData responseModel = new UserData(
                SerenityRest.lastResponse().jsonPath().getString("data.data.email"),
                SerenityRest.lastResponse().jsonPath().getString("data.data.first_name"),
                SerenityRest.lastResponse().jsonPath().getString("data.data.last_name")
        );
        analista.remember("userResponse", responseModel);
        analista.remember("sentData", new UserData(email, firstName, lastName));
    }

    @When("el analista elimina el usuario")
    public void elAnalistaEliminaElUsuario() {
        analista.attemptsTo(DeleteUser.withId(apiKey, recordId));
    }

    @Then("el codigo de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int expectedCode) {
        analista.should(
                seeThat(ResponseCode.value(), equalTo(expectedCode))
        );
    }

    @Then("la respuesta debe contener al menos un usuario")
    public void laRespuestaDebeContenerAlMenosUnUsuario() {
        analista.should(
                seeThat(UserListResponse.users(), hasSize(greaterThan(0)))
        );
    }

    @And("cada usuario debe tener email, first_name y last_name")
    public void cadaUsuarioDebeTenerCampos() {
        analista.should(
                seeThat(UserListResponse.users(), everyItem(hasKey("data")))
        );
    }

    @And("la respuesta debe contener un id generado")
    public void laRespuestaDebeContenerUnId() {
        analista.should(
                seeThat(UserFieldResponse.recordId(), is(notNullValue()))
        );
    }

    @And("los datos del usuario deben coincidir con los enviados")
    public void losDatosDebenCoincidirConLosEnviados() {
        UserData sent = analista.recall("sentData");
        analista.should(
                seeThat(UserFieldResponse.firstName(), equalTo(sent.getFirstName())),
                seeThat(UserFieldResponse.lastName(), equalTo(sent.getLastName())),
                seeThat(UserFieldResponse.email(), equalTo(sent.getEmail()))
        );
    }

    @And("la respuesta debe contener un mensaje de error")
    public void laRespuestaDebeContenerError() {
        analista.should(
                seeThat(ErrorResponse.message(), is(notNullValue()))
        );
    }

    @And("los datos actualizados deben coincidir con los enviados")
    public void losDatosActualizadosDebenCoincidir() {
        UserData sent = analista.recall("sentData");
        analista.should(
                seeThat(UserFieldResponse.firstName(), equalTo(sent.getFirstName())),
                seeThat(UserFieldResponse.lastName(), equalTo(sent.getLastName())),
                seeThat(UserFieldResponse.email(), equalTo(sent.getEmail()))
        );
    }

    @And("el id del usuario debe mantenerse igual")
    public void elIdDebeMantenerse() {
        analista.should(
                seeThat(UserFieldResponse.recordId(), equalTo(recordId))
        );
    }
}
