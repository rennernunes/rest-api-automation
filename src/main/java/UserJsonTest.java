import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class UserJsonTest {
  final String url_us = "https://restapi.wcaquino.me/users/";
  final String url_us1 = "https://restapi.wcaquino.me/users/1";
  final String url_us2 = "https://restapi.wcaquino.me/users/2";
  final String url_us3 = "https://restapi.wcaquino.me/users/3";
  final String url_us4 = "https://restapi.wcaquino.me/users/4";
  private JsonPath jsonPath;

  @Test
  public void firstLevelJson() {
    RestAssured
        .given()
        .when().get(url_us1)
        .then()
        .statusCode(200)
        .body("id", Matchers.is(1))
        .body("name", Matchers.containsString("Silva"))
        .body("age", Matchers.greaterThan(18));
  }

  @Test
  public void firstLevelJsonOtherForms() {
    Response response = RestAssured.request(Method.GET, url_us1);

    //path
    Assert.assertEquals(new Integer(1), response.path("id"));

    //jsonpath
    JsonPath jsonPath = new JsonPath(response.asString());
    Assert.assertEquals(1, jsonPath.getInt("id"));

    //from
    int id = JsonPath.from(response.asString()).getInt("id");
    Assert.assertEquals(1, id);
  }

  //verifica JSON com segundo nível
  @Test
  public void secondLevelJson() {
    RestAssured
        .given()
        .when().get(url_us2)
        .then()
        .statusCode(200)
        .body("id", Matchers.is(2))
        .body("name", Matchers.containsString("Joaquina"))
        .body("endereco.rua", Matchers.is("Rua dos bobos"));
  }

  //verifica JSON com lista contendo mesmo atributo
  @Test
  public void thirdLevelJsonList() {
    RestAssured
        .given()
        .when().get(url_us3)
        .then()
        .statusCode(200)
        .body("id", Matchers.is(3))
        .body("name", Matchers.containsString("Ana"))
        .body("filhos", Matchers.hasSize(2))
        .body("filhos[0].name", Matchers.is("Zezinho"))
        .body("filhos[1].name", Matchers.is("Luizinho"))
        .body("filhos.name", Matchers.hasItem("Luizinho"))
        .body("filhos.name", Matchers.hasItems("Luizinho", "Zezinho"))
    ;

    Response response = RestAssured.request(Method.GET, url_us3);
    System.out.println(response.asString());
    System.out.println(response.getBody().prettyPrint());
  }

  @Test
  public void returnsNoUserError() {
    RestAssured
        .given()
        .when().get(url_us4)
        .then()
        .statusCode(404)
        .body("error", Matchers.is("Usuário inexistente"))
    ;

    Response response = RestAssured.request(Method.GET, url_us4);
    System.out.println(response.getBody().prettyPrint());
  }

  @Test
  public void verifyCompleteJsonList() {

    System.out.println(RestAssured.get(url_us).prettyPrint());

    RestAssured
        .given()
        .when().get(url_us)
        .then()
        .statusCode(200)
        .body("$", Matchers.hasSize(3)) //$ procura na raiz do JSON
        .body("name", Matchers.hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
        .body("age[1]", Matchers.is(25)) //idade do segundo registro
        .body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho", "Luizinho")))
        .body("salary", Matchers.contains(1234.5677f, 2500, null))
    ;
  }

  /**
   * As anotações são baseadas no Groovy
   */
  @Test
  public void advancedValidationJson() {
    RestAssured
        .given()
        .when().get(url_us)
        .then()
        .statusCode(200)
        .body("$", Matchers.hasSize(3)) //$ procura na raiz do JSON
        .body("age.findAll{it <= 25}.size()", Matchers.is(2))
        .body("age.findAll{it <= 25 && it > 20}.size()", Matchers.is(1))
        .body("findAll{it.age <= 25 && it.age > 20}.name", Matchers.hasItem("Maria Joaquina")) //findAll procura desde o inicio do nível do JSON
        .body("findAll{it.age <= 25}[0].name", Matchers.is("Maria Joaquina"))
        .body("findAll{it.age <= 25}[-1].name", Matchers.is("Ana Júlia")) //-1 é o ultimo registro
        .body("find{it.age <= 25}.name", Matchers.is("Maria Joaquina")) //find traz apenas o primeiro elemento
        .body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Júlia"))
        .body("findAll{it.name.length() > 10}.name", Matchers.hasItems("Maria Joaquina", "João da Silva"))
        .body("name.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
        .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
        .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", Matchers.allOf(Matchers.arrayContaining("MARIA JOAQUINA"), Matchers.arrayWithSize(1)))
        .body("age.collect{it * 2}", Matchers.hasItems(60, 50, 40))
        .body("id.max()", Matchers.is(3))
        .body("salary.min()", Matchers.is(1234.5677f))
        .body("salary.findAll{it != null}.sum()", Matchers.is(Matchers.closeTo(3734.5677f, 0.001))) //closeTo passa margem de erri
        .body("salary.findAll{it != null}.sum()", Matchers.allOf(Matchers.greaterThan(3000d), Matchers.lessThan(5000d)))
    ;

    System.out.println(RestAssured.get(url_us).prettyPrint());
  }

  //simplificando as valikdações com JsonPath
  @Test
  public void joinJsonPathWithJava() {
    ArrayList<String> names =
        RestAssured
            .given()
            .when().get(url_us)
            .then()
            .statusCode(200)
            .extract().path("name.findAll{it.startsWith('Maria')}");

    Assert.assertEquals(1, names.size());
    Assert.assertTrue(names.get(0).equalsIgnoreCase("marIA jOaQuina")); //ignora case sensitive
    Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
  }
}
