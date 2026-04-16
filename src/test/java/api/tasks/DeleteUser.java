package api.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteUser implements Task {

    private final String apiKey;
    private final String recordId;

    public DeleteUser(String apiKey, String recordId) {
        this.apiKey = apiKey;
        this.recordId = recordId;
    }

    @Override
    @Step("{0} elimina el usuario con id {2}")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Delete.from("/api/collections/users/records/" + recordId)
                        .with(req -> req
                                .header("x-api-key", apiKey))
        );
    }

    public static DeleteUser withId(String apiKey, String recordId) {
        return instrumented(DeleteUser.class, apiKey, recordId);
    }
}