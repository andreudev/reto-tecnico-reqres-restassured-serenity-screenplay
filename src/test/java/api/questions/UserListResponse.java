package api.questions;

import io.restassured.path.json.JsonPath;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.util.List;
import java.util.Map;

public class UserListResponse implements Question<List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> answeredBy(Actor actor) {
        JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
        return jsonPath.getList("data");
    }

    public static UserListResponse users() {
        return new UserListResponse();
    }
}