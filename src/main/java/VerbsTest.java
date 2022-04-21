import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

public class VerbsTest {

  final String url_us = "https://restapi.wcaquino.me/users";

  @Test
  public void saveUserJsonPost(){
    RestAssured
        //objeto deve ser tratado como JSON e enviado pelo POST
        .given().log().all().contentType("application/json").body("{\"name\":\"Jose\", \"age\":50}")
        .when().post(url_us)
        .then().log().all().statusCode(201) //objeto foi criado
        .body("id", Matchers.is(Matchers.notNullValue()))
        .body("name", Matchers.is("Jose"))
        .body("age", Matchers.is(50))
        ;
  }


}
