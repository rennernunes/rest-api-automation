import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StaticAttributes {

  @BeforeClass
  public static void setup() {

    RestAssured.baseURI = "https://restapi.wcaquino.me";
    RestAssured.port = 443; //opcional a porta
    RestAssured.basePath = "";
  }

  @Test
  public void staticUrlApiV2() {

    RestAssured.baseURI = "http://restapi.wcaquino.me";
    RestAssured.port = 80; //opcional a porta
    RestAssured.basePath = "/v2";

    RestAssured
        .given().log().all() //loga a requisição
        .when().get("/users") //passa apenas o recurso
        .then()
        .statusCode(200)
    ;
  }

  @Test
  public void staticUrlXML() {

    RestAssured
        .given().log().all() //loga a requisição
        .when().get("/usersXML") //passa apenas o recurso
        .then()
        .statusCode(200)
    ;
  }

  @Test
  public void staticUrlThirdLevelXML() {

    RestAssured
        .given().log().all() //loga a requisição
        .when().get("/usersXML/3") //passa apenas o recurso
        .then()
        .statusCode(200)

        .rootPath("user")
        .body("name", Matchers.is("Ana Julia"))
        .body("@id", Matchers.is("3"))
        .rootPath("user.filhos")
        .body("name.size()", Matchers.is(2))
        .body("name[0]", Matchers.is("Zezinho"))
        .body("name[1]", Matchers.is("Luizinho"))
        .body("name", Matchers.hasItem("Luizinho"))
        .body("name", Matchers.hasItems("Zezinho", "Luizinho"))
    ;
  }

}
