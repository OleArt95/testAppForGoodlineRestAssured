import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;

public class VkTestClass {

    @Test
    public void  getUserInfoWithMethodUsersGet() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.header("Content-Type", String.valueOf(ContentType.JSON));

        requestSpecification.header( "user ids", "ole_art" );

        requestSpecification.param("access_token", "b4f2daabf13912c20411dd88eb3ca5fd6b23d8b1fca2c8f003efcc6dcb0d89f2968820eab8a4f099e8746");
        requestSpecification.param("v", "5.52");

        Response response = requestSpecification.get("https://api.vk.com/method/users.get");

        System.out.println(response.getBody().asString());
    }
}
