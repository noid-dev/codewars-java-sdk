package dev.noid.codewars.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.noid.codewars.client.ApiException;
import dev.noid.codewars.client.ApiResponse;
import dev.noid.codewars.client.model.User;
import org.junit.jupiter.api.Test;

class UsersApiIT {

  private final UsersApi api = new UsersApi();
  private final User test = Stubs.read("user-test.json", User.class);
  private final User nobilo = Stubs.read("user-nobilo.json", User.class);

  @Test
  void getUserById() {
    assertEquals(test, api.getUser("604ef9a5ac4fd00011f78eb0"));
  }

  @Test
  void getUserByName() {
    assertEquals(test, api.getUser("test-user"));
  }

  @Test
  void getRecentlyCreatedUser() {
    assertEquals(nobilo, api.getUser("nobilo.imifel"));
  }

  @Test
  void getUserWithHttpInfo() {
    ApiResponse<User> userResponse = api.getUserWithHttpInfo("test-user");
    assertEquals(200, userResponse.getStatusCode());
    assertEquals(test, userResponse.getData());
  }

  @Test
  void getNonexistentUser() {
    ApiException error = assertThrows(ApiException.class, () -> api.getUser("test-user-not-found"));
    assertEquals(404, error.getCode());
    assertEquals("{\"success\":false,\"reason\":\"not found\"}", error.getResponseBody());
  }

  @Test
  void getUserWithNullParam() {
    ApiException error = assertThrows(ApiException.class, () -> api.getUser(null));
    assertEquals(400, error.getCode());
    assertNull(error.getResponseBody());
    assertEquals("Missing the required parameter 'user' when calling getUser", error.getMessage());
  }
}
