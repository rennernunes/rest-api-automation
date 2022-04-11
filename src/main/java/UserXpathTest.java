import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class UserXpathTest {

  final String url_us = "https://restapi.wcaquino.me/usersXML/";
  final String url_us1 = "https://restapi.wcaquino.me/usersXML/1";
  final String url_us2 = "https://restapi.wcaquino.me/usersXML/2";
  final String url_us3 = "https://restapi.wcaquino.me/usersXML/3";
  final String url_us4 = "https://restapi.wcaquino.me/usersXML/4";

  @Test
  public void advancedValidationXPath() {
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


}
