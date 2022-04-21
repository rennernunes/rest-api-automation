import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Utiliza Request/ResponseSpec para reutilizar em outros cenários
 */
public class RequestResponseSpecBuilderTest {

  public static RequestSpecification requestSpecification;
  public static ResponseSpecification responseSpecification;

  @BeforeClass
  public static void setup() {
    RestAssured.baseURI = "https://restapi.wcaquino.me";

    //trata a requisição
    RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
    requestBuilder.log(LogDetail.ALL);
    requestSpecification = requestBuilder.build();

    //trata a resposta
    ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
    responseBuilder.expectStatusCode(200);
    responseSpecification = responseBuilder.build();

    //Não precisa mais adicionar a especificação aos testes
    RestAssured.requestSpecification =  requestSpecification;
    RestAssured.responseSpecification = responseSpecification;
  }

  @Test
  public void requestSpecThirdLevelXML() {

    RestAssured
        //.given().spec(requestSpecification)
        .given()
        .when().get("/usersXML/3") //passa apenas o recurso
        //.then().spec(responseSpecification)
        .then()

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
