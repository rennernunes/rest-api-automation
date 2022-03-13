import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


public class UserJsonTest {
  final String url_fl = "https://restapi.wcaquino.me/users/1";
  private JsonPath jsonPath;

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

  @Test
  public void firstLevelJsonOtherForms() {
    Response response = RestAssured.request(Method.GET, url_fl);

    //path
    Assert.assertEquals(new Integer(1), response.path("id"));

    //jsonpath
    JsonPath jsonPath = new JsonPath(response.asString());
    Assert.assertEquals(1, jsonPath.getInt("id"));

    //from
    int id = JsonPath.from(response.asString()).getInt("id");
    Assert.assertEquals(1, id);
  }
}
