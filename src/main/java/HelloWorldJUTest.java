import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HelloWorldJUTest {

  String url = "http://restapi.wcaquino.me/ola";
  String url_google = "https://www.google.com.br:443/search?q=testes+de+api";

  @Test
  public void hellorWorldTest() {
    //requisição body e status code
    Response response = io.restassured.RestAssured.request(Method.GET, url);
    System.out.println(response.getBody().asString());
    System.out.println(response.statusCode());
    Assert.assertEquals(200, response.statusCode());

    //valida o status code recebido
    ValidatableResponse valida = response.then();
    valida.statusCode(200);
  }

  @Test
  public void simplifyingPreviousMethod() {
    RestAssured.get(url).then().statusCode(200);
  }

  @Test
  public void bddRestAssured() {
    RestAssured
        .given()
        .when()
        .get(url)
        .then()
        .statusCode(200);
  }

  @Test
  public void matchersHamcrest() {
    Assert.assertThat("Renner", Matchers.is("Renner"));
    Assert.assertThat("Renner", Matchers.not("Rick"));
    Assert.assertThat("Renner", Matchers.anyOf(Matchers.is("Rick"), Matchers.is("Renner")));
    Assert.assertThat("Renner", Matchers.allOf(Matchers.startsWith("Ren"), Matchers.endsWith("ner"), Matchers.containsString("nn")));
    Assert.assertThat(128, Matchers.is(128));
    Assert.assertThat(128, Matchers.isA(Integer.class));
    Assert.assertThat(128d, Matchers.isA(Double.class));
    Assert.assertThat(128d, Matchers.greaterThan(120d));
    Assert.assertThat(128d, Matchers.lessThan(130d));

    List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
    Assert.assertThat(impares, Matchers.hasSize(5));
    Assert.assertThat(impares, Matchers.contains(1, 3, 5, 7, 9));
    Assert.assertThat(impares, Matchers.containsInAnyOrder(3, 5, 9, 7, 1));
    Assert.assertThat(impares, Matchers.hasItem(1));
    Assert.assertThat(impares, Matchers.hasItems(1, 9));
  }

  @Test
  public void validateBody() {
    RestAssured
        .given()
        .when().get(url)
        .then()
        .statusCode(200)
        .body(Matchers.is("Ola Mundo!"))
        .body(Matchers.containsString("Mundo"))
        .body(Matchers.not(Matchers.nullValue()));
  }
}
