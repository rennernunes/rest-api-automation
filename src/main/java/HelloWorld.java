import io.restassured.http.Method;
import io.restassured.response.Response;

public class HelloWorld {

  public static void main(String[] args) {
    String url = "http://restapi.wcaquino.me/ola";
    String url_google = "https://www.google.com.br:443/search?q=testes+de+api";

    Response response = io.restassured.RestAssured.request(Method.GET, url_google);
    System.out.println(response.getBody().asString());
    
  }
}
