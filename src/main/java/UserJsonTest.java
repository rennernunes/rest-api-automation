import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

public class UserJsonTest {
  final String url_fl = "https://restapi.wcaquino.me/users/1";

  @Test
  public void firstLevelJson() {
    RestAssured
        .given()
        .when().get(url_fl)
        .then()
        .statusCode(200)
        .body("id", Matchers.is(1))
        .body("name", Matchers.containsString("Silva"))
        .body("age", Matchers.greaterThan(18));
  }
}
