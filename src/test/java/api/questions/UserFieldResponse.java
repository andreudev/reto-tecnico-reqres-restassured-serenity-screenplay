package api.questions;


import io.restassured.path.json.JsonPath;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class UserFieldResponse {

    public static Question<String> firstName() {
        return actor -> {
            JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
            return jsonPath.getString("data.data.first_name");
        };
    }

    public static Question<String> lastName() {
        return actor -> {
            JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
            return jsonPath.getString("data.data.last_name");
        };
    }

    public static Question<String> email() {
        return actor -> {
            JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
            return jsonPath.getString("data.data.email");
        };
    }

    public static Question<String> recordId() {
        return actor -> {
            JsonPath jsonPath = SerenityRest.lastResponse().jsonPath();
            return jsonPath.getString("data.id");
        };
    }

}