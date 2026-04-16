package api.questions;

import io.restassured.path.json.JsonPath;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ErrorResponse implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
        return jsonPath.getString("message");
    }

    public static ErrorResponse message() {
        return new ErrorResponse();
    }
}
