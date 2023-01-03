package dev.noid.codewars.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.noid.codewars.client.ApiException;
import dev.noid.codewars.client.ApiResponse;
import dev.noid.codewars.client.model.CompletedChallenges;
import org.junit.jupiter.api.Test;

class CompletedChallengesApiIT {

  private final UsersApi api = new UsersApi();
  private final CompletedChallenges page1 = Stubs.read("completed-challenges-p1.json", CompletedChallenges.class);
  private final CompletedChallenges page2 = Stubs.read("completed-challenges-p2.json", CompletedChallenges.class);
  private final CompletedChallenges blank = Stubs.read("completed-challenges-blank.json", CompletedChallenges.class);

  @Test
  void listCompletedChallengesAtNullPage() {
    assertEquals(page1, api.listCompletedChallenges("test-user", null));
  }

  @Test
  void listCompletedChallengesAtNegativePage() {
    assertEquals(page1, api.listCompletedChallenges("test-user", -1));
  }

  @Test
  void listCompletedChallengesAtFirstPage() {
    assertEquals(page1, api.listCompletedChallenges("test-user", 0));
  }

  @Test
  void listCompletedChallengesAtSecondPage() {
    assertEquals(page2, api.listCompletedChallenges("test-user", 2));
  }

  @Test
  void listCompletedChallengesForNewUser() {
    assertEquals(blank, api.listCompletedChallenges("nobilo.imifel", 0));
  }

  @Test
  void listCompletedChallengesWithHttpInfo() {
    ApiResponse<CompletedChallenges> completedResponse = api.listCompletedChallengesWithHttpInfo("test-user", 0);
    assertEquals(200, completedResponse.getStatusCode());
    assertEquals(page1, completedResponse.getData());
  }

  @Test
  void listCompletedChallengesForNonexistentUser() {
    ApiException error = assertThrows(ApiException.class, () -> api.listCompletedChallenges("test-user-not-found", 0));
    assertEquals(404, error.getCode());
    assertEquals("{\"success\":false,\"reason\":\"not found\"}", error.getResponseBody());
  }

  @Test
  void listCompletedChallengesForNullUser() {
    ApiException error = assertThrows(ApiException.class, () -> api.listCompletedChallenges(null, 0));
    assertEquals(400, error.getCode());
    assertNull(error.getResponseBody());
    assertEquals("Missing the required parameter 'user' when calling listCompletedChallenges", error.getMessage());
  }
}
