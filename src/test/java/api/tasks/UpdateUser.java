package api.tasks;

import api.models.UserData;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateUser implements Task {

    private final String apiKey;
    private final String recordId;
    private final UserData userData;

    public UpdateUser(String apiKey, String recordId, UserData userData) {
        this.apiKey = apiKey;
        this.recordId = recordId;
        this.userData = userData;
    }

    @Override
    @Step("{0} actualiza el usuario con id {2}")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Put.to("/api/collections/users/records/" + recordId)
                        .with(req -> req
                                .header("x-api-key", apiKey)
                                .header("Content-Type", "application/json")
                                .body(Map.of("data", userData)))
        );
    }

    public static UpdateUser withData(String apiKey, String recordId, String email, String firstName, String lastName) {
        return instrumented(UpdateUser.class, apiKey, recordId, new UserData(email, firstName, lastName));
    }
}