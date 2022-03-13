import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;

public class HelloWorld {

  public static void main(String[] args) {
    String url = "http://restapi.wcaquino.me/ola";
    String url_google = "https://www.google.com.br:443/search?q=testes+de+api";

    //requisição body e status code
    Response response = io.restassured.RestAssured.request(Method.GET, url_google);
    System.out.println(response.getBody().asString());
    System.out.println(response.statusCode());

    //valida o status code recebido
    ValidatableResponse valida =  response.then();
    valida.statusCode(200);
  }
}
