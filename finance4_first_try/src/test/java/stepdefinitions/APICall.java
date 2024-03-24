package stepdefinitions;

import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static config.Config.baseUrl;
import static config.Config.endPoint;
import static io.restassured.RestAssured.given;

public class APICall {
public static String authorFromApi ;
    @And("get author from API")
    public void   getRequestWithQueryParam() {
    RestAssured.baseURI = baseUrl;
        Response response = given()
                .when()
                .get(endPoint)
                .then()
                .extract().response();
       assertThat(200, equalTo(response.statusCode()));

        String htmlContent = response.getBody().asString();
System.out.println(htmlContent);
        // Parse the HTML content using Jsoup
        Document doc = Jsoup.parse(htmlContent);
        authorFromApi = doc.select("a[itemprop='author']").first().text();
    }
}
