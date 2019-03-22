import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class TestVkClass {

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
                .addParam("access_token", "6132d677a3a7e3eb995a6793cc5c1ecf7c94f98eb77222bc7e902710d4e68a35807ec5e206223abad893f")
                .addParam("v", API_VERSION)
                .build();

        RestAssured.requestSpecification = basicSpecification;
    }

    @Test
    public void test_get_user_info_with_method_users_get() {
        given().
                param("user_ids", "ole_art").
        when().
                log().all().
                get(API_VK_USERS_GET).
        then().
                assertThat().
                body(matchesJsonSchemaInClasspath("UsersMethodGetSchema.json")).
                assertThat().statusCode(HTTP_STATUS_OK);

        System.out.println("testGetUserInfoWithMethodUsersGet");
    }

    @Test
    public void get_user_info_with_ethod_user_get_with_fields() {
        String expectedCity = "Кемерово";
        String expectedFirstName = "Олег";
        String expectedLastName = "Артюхов";
        int expectedSex = 2;

        given().
                param("user_ids", "ole_art").
                param("fields", "city, sex").
        when().
                get(API_VK_USERS_GET).
        then().
                assertThat().
                body(matchesJsonSchemaInClasspath("UsersMethodGetWithFieldsSchema.json")).
                assertThat().statusCode(HTTP_STATUS_OK).
                assertThat().body("response[0].city.title", equalTo(expectedCity)).
                assertThat().body("response[0].sex", equalTo(expectedSex)).
                assertThat().body("response[0].first_name", equalTo(expectedFirstName)).
                assertThat().body("response[0].last_name", equalTo(expectedLastName));

        System.out.println("getUserInfoWithMethodUserGetWithFields");
    }

    @Ignore
    @Test
    public void get_info_with_method_search_get_hints() {
        given().
                param("q", "Типичный Кемерово").
                param("limit", 10).
                param("search_global", 1).
        when().
                get(API_VK_SEARCH_GET_HINTS).
        then().
                assertThat().
                body(matchesJsonSchemaInClasspath("SearchMethodGetHints.json")).
                assertThat().statusCode(HTTP_STATUS_OK);

        System.out.println("getInfoWithMethodSearchGetHints");
    }

    @Test
    public void get_info_with_method_users_search() {
        String expectedLastName = "Леонов";

        given().
                param("q", "Алексей Леонов").
                param("count", "4").
                param("hometown", "Кемерово").
        when().
                get(API_VK_USERS_SEARCH).
        then().
                assertThat().
                body(matchesJsonSchemaInClasspath("UsersMethodGetWithFieldsSchema.json")).
                assertThat().statusCode(HTTP_STATUS_OK).
                assertThat().body("response.items[2].last_name", equalTo(expectedLastName));

        System.out.println("getInfoWithMethodSearchGetHints");
    }

    @Test
    public void get_info_with_method_users_get_followers() {
        String expectedLastName = "Лазина";

        given().
                param("user_id", "228270452").
                param("count", "2").
                param("fields", "first_name, last_name").
        when().
                get(API_VK_USERS_GET_FOLLOWERS).
        then().
                assertThat().
                body(matchesJsonSchemaInClasspath("UsersMethodGetFollowersSchema.json")).
                assertThat().statusCode(HTTP_STATUS_OK).
                assertThat().body("response.items[1].last_name", equalTo(expectedLastName));

        System.out.println("getInfoWithMethodUsersGetFollowers");
    }
}
