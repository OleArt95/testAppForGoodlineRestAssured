import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class VkTestClass {

    private static final int HTTP_STATUS_OK = 200;
    private static final String URL_API_VK_COM = "https://api.vk.com";
    private static final String BASE_PATH = "/method/";
    private static final String API_VERSION = "5.52";

    private static final String API_VK_USERS_GET = "users.get";
    private static final String API_VK_SEARCH_GET_HINTS = "search.getHints";
    private static final String API_VK_USERS_SEARCH = "users.search";
    private static final String API_VK_USERS_GET_FOLLOWERS = "users.getFollowers";

    @Before
    public void configuration() {
    RequestSpecification basicSpecification = new RequestSpecBuilder()
            .setBaseUri(URL_API_VK_COM)
            .setBasePath(BASE_PATH)
            .setContentType(ContentType.JSON)
            .addParam("access_token","b4f2daabf13912c20411dd88eb3ca5fd6b23d8b1fca2c8f003efcc6dcb0d89f2968820eab8a4f099e8746")
            .addParam("v", API_VERSION)
            .build();

    RestAssured.requestSpecification = basicSpecification;
    }

    @Test
    public void getUserInfoWithMethodUsersGet() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.param("user_ids", "ole_art");

        Response response = requestSpecification.get(API_VK_USERS_GET);

        System.out.println(response.getBody().asString());

        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
    }

    @Test
    public void getUserInfoWithMethodUserGetWithFields() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.param("user_ids", "ole_art");
        requestSpecification.param("fields", "city, sex");

        Response response = requestSpecification.get(API_VK_USERS_GET);

        System.out.println(response.getBody().asString());
        System.out.println(response.path("response[0].city"));

        Assert.assertEquals("Кемерово", response.path("response[0].city.title"));
        Assert.assertEquals(2, response.path("response[0].sex"));
        Assert.assertEquals("Олег", response.path("response[0].first_name"));
        Assert.assertEquals("Артюхов", response.path("response[0].last_name"));
        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
    }

    @Ignore
    @Test
    public void getInfoWithMethodSearchGetHints() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.param("q", "Типичный Кемерово");
        requestSpecification.param("limit", "10");
        requestSpecification.param("search_global", 1);

        Response response = requestSpecification.get(API_VK_SEARCH_GET_HINTS);

        System.out.println(response.getBody().asString());
    }

    @Test
    public void getInfoWithMethodUsersSearch() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.param("q", "Алексей Леонов");
        requestSpecification.param("count", "4");
        requestSpecification.param("hometown", "Кемерово");

        Response response = requestSpecification.get(API_VK_USERS_SEARCH);

        System.out.println(response.getBody().asString());

        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
        Assert.assertEquals("Леонов", response.path("response.items[2].last_name"));
    }

    @Test
    public void getInfoWithMethodUsersGetFollowers() {
        RequestSpecification requestSpecification = RestAssured.given();

        requestSpecification.param("user_id", "228270452");
        requestSpecification.param("count", "2");
        requestSpecification.param("fields", "first_name, last_name");

        Response response = requestSpecification.get(API_VK_USERS_GET_FOLLOWERS);

        System.out.println(response.getBody().asString());

        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
        Assert.assertEquals("Лазина", response.path("response.items[1].last_name"));
    }
}
