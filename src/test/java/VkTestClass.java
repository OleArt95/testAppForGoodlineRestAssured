import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

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
                .addParam("access_token", "e2aac7c6aacb02ade526337a568564938500686dc204bde26160d054cd0c542fa8060b63134ded7fa88b2")
                .addParam("v", API_VERSION)
                .build();

        RestAssured.requestSpecification = basicSpecification;
    }

    @Test
    public void getUserInfoWithMethodUsersGet() {
        given().
                param("user_ids", "ole_art")

                .when().
                get(API_VK_USERS_GET)
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("UsersMethodGetSchema.json"))
                .assertThat().statusCode(HTTP_STATUS_OK);
    }

    @Test
    public void getUserInfoWithMethodUserGetWithFields() {
        String expectedCity = "Кемерово";
        String expectedFirstName = "Олег";
        String expectedLastName = "Артюхов";
        int expectedSex = 2;

        given().
                param("user_ids", "ole_art").
                param("fields", "city, sex").
        when().
                get(API_VK_USERS_GET)
        .then()
                .assertThat()
                .body(Users)
//        RequestSpecification requestSpecification = given();
//
//        requestSpecification.param("user_ids", "ole_art");
//        requestSpecification.param("fields", "city, sex");
//
//        Response response = requestSpecification.get(API_VK_USERS_GET);
//
//        System.out.println(response.getBody().asString());
//        System.out.println(response.path("response[0].city"));
//
//        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
//        Assert.assertEquals(expectedCity, response.path("response[0].city.title"));
//        Assert.assertEquals(expectedSex, response.path("response[0].sex"));
//        Assert.assertEquals(expectedFirstName, response.path("response[0].first_name"));
//        Assert.assertEquals(expectedLastName, response.path("response[0].last_name"));
    }

    @Ignore
    @Test
    public void getInfoWithMethodSearchGetHints() {
        RequestSpecification requestSpecification = given();

        requestSpecification.param("q", "Типичный Кемерово");
        requestSpecification.param("limit", "10");
        requestSpecification.param("search_global", 1);

        Response response = requestSpecification.get(API_VK_SEARCH_GET_HINTS);

        System.out.println(response.getBody().asString());
    }

    @Test
    public void getInfoWithMethodUsersSearch() {
        String expectedLastName = "Леонов";

        RequestSpecification requestSpecification = given();

        requestSpecification.param("q", "Алексей Леонов");
        requestSpecification.param("count", "4");
        requestSpecification.param("hometown", "Кемерово");

        Response response = requestSpecification.get(API_VK_USERS_SEARCH);

        System.out.println(response.getBody().asString());

        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
        Assert.assertEquals(expectedLastName, response.path("response.items[2].last_name"));
    }

    @Test
    public void getInfoWithMethodUsersGetFollowers() {
        String expectedLastName = "Лазина";

        RequestSpecification requestSpecification = given();

        requestSpecification.param("user_id", "228270452");
        requestSpecification.param("count", "2");
        requestSpecification.param("fields", "first_name, last_name");

        Response response = requestSpecification.get(API_VK_USERS_GET_FOLLOWERS);

        System.out.println(response.getBody().asString());

        Assert.assertEquals(HTTP_STATUS_OK, response.statusCode());
        Assert.assertEquals(expectedLastName, response.path("response.items[1].last_name"));
    }
}
