package api.tasks;

import api.models.UserData;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateUser implements Task {

    private final String apiKey;
    private final UserData userData;
    private final boolean sendInvalidBody;

    public CreateUser(String apiKey, UserData userData, boolean sendInvalidBody) {
        this.apiKey = apiKey;
        this.userData = userData;
        this.sendInvalidBody = sendInvalidBody;
    }

    @Override
    @Step("{0} registra un nuevo usuario")
    public <T extends Actor> void performAs(T actor) {
        Object body = sendInvalidBody ? Map.of() : Map.of("data", userData);
        actor.attemptsTo(
                Post.to("/api/collections/users/records")
                        .with(req -> req
                                .header("x-api-key", apiKey)
                                .header("Content-Type", "application/json")
                                .body(body))
        );
    }

    public static CreateUser withData(String apiKey, String email, String firstName, String lastName) {
        return instrumented(CreateUser.class, apiKey, new UserData(email, firstName, lastName), false);
    }

    public static CreateUser withEmptyData(String apiKey) {
        return instrumented(CreateUser.class, apiKey, new UserData(), true);
    }
}