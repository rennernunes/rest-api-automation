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
    RestAssured
        .given()
        .when().get(url_us)
        .then()
        .statusCode(200)
        .body(Matchers.hasXPath("count(/users/user)", Matchers.is("3")))
        .body(Matchers.hasXPath("/users/user[@id='1']"))
        .body(Matchers.hasXPath("//user[@id='2']")) //outra forma
        .body(Matchers.hasXPath("//name[text()='Luizinho']"))
        .body(Matchers.hasXPath("//name[text()='Luizinho']/../../name", Matchers.is("Ana Julia"))) // /.. sobe um nível / desce. A partir dos filhos obter nome da mão
        .body(Matchers.hasXPath("//name[text()='Ana Julia']/following-sibling::filhos", Matchers.allOf(Matchers.containsString("Zezinho"), Matchers.containsString("Luizinho")))) // A partir da mãe obter o nome dos filhos
        .body(Matchers.hasXPath("//user/name", Matchers.is("João da Silva"))) //pegar o primeiro nome
        .body(Matchers.hasXPath("//user[2]/name", Matchers.is("Maria Joaquina"))) //pegar o segundo nome
        .body(Matchers.hasXPath("//user[last()]/name", Matchers.is("Ana Julia"))) //pegar o penultimo nome
        .body(Matchers.hasXPath("count(//user/name[contains(., 'n')])", Matchers.is("2"))) //pessoas com N no nome
        .body(Matchers.hasXPath("//user[age < 24]/name", Matchers.is("Ana Julia")))
        .body(Matchers.hasXPath("//user[age > 20 and age <30 ]/name", Matchers.is("Maria Joaquina")))
    ;
  }


}
