package api.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ListUsers implements Task {

    private final String apiKey;

    public ListUsers(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    @Step("{0} solicita la lista de usuarios")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/collections/users/records")
                        .with(req -> req
                                .header("x-api-key", apiKey)
                                .queryParam("page", 1)
                                .queryParam("limit", 10))
        );
    }

    public static ListUsers withApiKey(String apiKey) {
        return instrumented(ListUsers.class, apiKey);
    }
}