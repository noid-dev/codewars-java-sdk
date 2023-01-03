package dev.noid.codewars.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.noid.codewars.client.ApiException;
import dev.noid.codewars.client.ApiResponse;
import dev.noid.codewars.client.model.AuthoredChallenges;
import org.junit.jupiter.api.Test;

class AuthoredChallengesApiIT {

  private final UsersApi api = new UsersApi();
  private final AuthoredChallenges rbuckley = Stubs.read("authored-challenges-rbuckley.json", AuthoredChallenges.class);
  private final AuthoredChallenges blank = Stubs.read("authored-challenges-blank.json", AuthoredChallenges.class);

  @Test
  void listAuthoredChallengesForBuckley() {
    assertEquals(rbuckley, api.listAuthoredChallenges("rbuckley"));
  }

  @Test
  void listAuthoredChallengesForTestUser() {
    assertEquals(blank, api.listAuthoredChallenges("test-user"));
  }

  @Test
  void listAuthoredChallengesWithHttpInfo() {
    ApiResponse<AuthoredChallenges> authoredResponse = api.listAuthoredChallengesWithHttpInfo("rbuckley");
    assertEquals(200, authoredResponse.getStatusCode());
    assertEquals(rbuckley, authoredResponse.getData());
  }

  @Test
  void listAuthoredChallengesForNonexistentUser() {
    ApiException error = assertThrows(ApiException.class, () -> api.listAuthoredChallenges("test-user-not-found"));
    assertEquals(404, error.getCode());
    assertEquals("{\"success\":false,\"reason\":\"not found\"}", error.getResponseBody());
  }

  @Test
  void listAuthoredChallengesForNullUser() {
    ApiException error = assertThrows(ApiException.class, () -> api.listAuthoredChallenges(null));
    assertEquals(400, error.getCode());
    assertNull(error.getResponseBody());
    assertEquals("Missing the required parameter 'user' when calling listAuthoredChallenges", error.getMessage());
  }
}
