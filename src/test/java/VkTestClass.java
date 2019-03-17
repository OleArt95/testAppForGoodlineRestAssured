import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.equalTo;

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
                .addParam("access_token", "647e0a47b8fdbc94c2a9fd1cc557bd5e9b136eab04bfce2b5fc93456b526b0ecc9f9125c7a95f26414fff")
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
                .body(matchesJsonSchemaInClasspath("UsersMethodGetWithFieldsSchema.json"))
                .assertThat().statusCode(HTTP_STATUS_OK)
                .assertThat().body("response[0].city.title", equalTo(expectedCity))
                .assertThat().body("response[0].sex", equalTo(expectedSex))
                .assertThat().body("response[0].first_name", equalTo(expectedFirstName))
                .assertThat().body("response[0].last_name", equalTo(expectedLastName));
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
