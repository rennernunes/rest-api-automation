import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

public class VerbsTest {

  final String url_us = "https://restapi.wcaquino.me/users";
  final String url_us_xml = "https://restapi.wcaquino.me/usersXML";

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

  @Test
  public void dontSaveUnnamedUser(){
    RestAssured
        //objeto deve ser tratado como JSON e enviado pelo POST
        .given().log().all().contentType("application/json").body("{\"age\":50}")
        .when().post(url_us)
        .then().log().all().statusCode(400) //objeto não foi criado
        .body("id", Matchers.is(Matchers.nullValue()))
        .body("error", Matchers.is("Name é um atributo obrigatório"))
        ;
  }

  @Test
  public void saveUserXMLPost(){
    RestAssured
        //objeto deve ser tratado como XML e enviado pelo POST
        .given().log().all().contentType(ContentType.XML).body("<user><name>Jose</name><age>50</age></user>")
        .when().post(url_us_xml)
        .then().log().all().statusCode(201) //objeto foi criado
        .body("user.@id", Matchers.is(Matchers.notNullValue()))
        .body("user.name", Matchers.is("Jose"))
        .body("user.age", Matchers.is("50")) //Numeros devem ser Strings no XML
    ;
  }



}
