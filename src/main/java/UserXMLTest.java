import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class UserXMLTest {

  final String url_us = "https://restapi.wcaquino.me/usersXML/";
  final String url_us1 = "https://restapi.wcaquino.me/usersXML/1";
  final String url_us2 = "https://restapi.wcaquino.me/usersXML/2";
  final String url_us3 = "https://restapi.wcaquino.me/usersXML/3";
  final String url_us4 = "https://restapi.wcaquino.me/usersXML/4";

  @Test
  public void thirdLevelXML() {
    Response response = RestAssured.request(Method.GET, url_us3);
    System.out.println(response.getBody().prettyPrint());

    RestAssured
        .given()
        .when().get(url_us3)
        .then()
        .statusCode(200)
        .body("user.name", Matchers.is("Ana Julia"))
        .body("user.@id", Matchers.is("3"))
        .body("user.filhos.name.size()", Matchers.is(2))
        .body("user.filhos.name[0]", Matchers.is("Zezinho"))
        .body("user.filhos.name[1]", Matchers.is("Luizinho"))
        .body("user.filhos.name", Matchers.hasItem("Luizinho"))
        .body("user.filhos.name", Matchers.hasItems("Zezinho", "Luizinho"))
    ;
  }

  @Test
  public void thirdLevelRootPathXML() {
    Response response = RestAssured.request(Method.GET, url_us3);
    System.out.println(response.getBody().prettyPrint());

    RestAssured
        .given()
        .when().get(url_us3)
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

  @Test
  public void thirdDetachAppendLevelRootPathXML() {
    Response response = RestAssured.request(Method.GET, url_us3);
    System.out.println(response.getBody().prettyPrint());

    RestAssured
        .given()
        .when().get(url_us3)
        .then()
        .statusCode(200)

        .rootPath("user")
        .body("name", Matchers.is("Ana Julia"))
        .body("@id", Matchers.is("3"))

        .rootPath("user.filhos")
        .body("name.size()", Matchers.is(2))

        .detachRootPath("filhos") //retira do rootPath o valor, deve declarar novamente
        .body("filhos.name[0]", Matchers.is("Zezinho"))
        .body("filhos.name[1]", Matchers.is("Luizinho"))

        .appendRootPath("filhos")// coloca o valor de volta
        .body("name", Matchers.hasItem("Luizinho"))
        .body("name", Matchers.hasItems("Zezinho", "Luizinho"))
    ;
  }

  @Test
  public void advancedValidationXML() {
    Response response = RestAssured.request(Method.GET, url_us);
    System.out.println(response.getBody().prettyPrint());

    RestAssured
        .given()
        .when().get(url_us)
        .then()
        .statusCode(200)
        .body("users.user.name.size()", Matchers.is(3))
        .body("users.user.findAll{it.age.toInteger() <= 25}.size()", Matchers.is(2))
        .body("users.user.@id", Matchers.hasItems("1", "2", "3")) //XML sempre utiliza string
        .body("users.user.find{it.age == 25}.name", Matchers.is("Maria Joaquina"))
        .body("users.user.findAll{it.name.toString().contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Julia"))
        .body("users.user.salary.find{it != null}", Matchers.is("1234.5678"))
        .body("users.user.salary.find{it != null}.toDouble()", Matchers.is(1234.5678d))
        .body("users.user.age.collect{it.toInteger() * 2}", Matchers.hasItems(40, 50, 60))
        .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", Matchers.is("MARIA JOAQUINA"))
    ;
  }

  //simplificando as validações com JsonPath
  @Test
  public void joinXMLPathWithJava() {
    String names =
        RestAssured
            .given()
            .when().get(url_us)
            .then()
            .statusCode(200)
            .extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");

    Assert.assertEquals("maria joaquina".toUpperCase(), names.toUpperCase());
  }
  //simplificando as validações com JsonPath
  @Test
  public void joinXMLObjectPathWithJava() {
//    Object path =
    ArrayList<NodeImpl> names = //XMl trabalha com NodeImpl, tem que fazer as conversões (casts)
        RestAssured
            .given()
            .when().get(url_us)
            .then()
            .statusCode(200)
            .extract().path("users.user.name.findAll{it.toString().contains('n')}");

    System.out.println(names);

    Assert.assertEquals(2, names.size());
    Assert.assertEquals("maria joaquina".toUpperCase(), names.get(0).toString().toUpperCase());
    Assert.assertTrue("ANA JULIA".equalsIgnoreCase(names.get(1).toString()));
  }

}
