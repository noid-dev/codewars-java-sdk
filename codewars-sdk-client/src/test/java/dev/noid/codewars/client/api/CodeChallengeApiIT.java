package dev.noid.codewars.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.noid.codewars.client.ApiException;
import dev.noid.codewars.client.ApiResponse;
import dev.noid.codewars.client.model.CodeChallenge;
import org.junit.jupiter.api.Test;

class CodeChallengeApiIT {

  private final CodeChallengesApi api = new CodeChallengesApi();
  private final CodeChallenge trivial = Stubs.read("challenge-a-plus-b.json", CodeChallenge.class);
  private final CodeChallenge beta = Stubs.read("challenge-camelcasing.json", CodeChallenge.class);
  private final CodeChallenge retired = Stubs.read("challenge-calculator.json", CodeChallenge.class);

  @Test
  void getCodeChallengeById() {
    assertEquals(trivial, api.getCodeChallenge("5512a0b0509063e57d0003f5"));
  }

  @Test
  void getCodeChallengeBySlug() {
    assertEquals(trivial, api.getCodeChallenge("a-plus-b"));
  }

  @Test
  void getBetaChallenge() {
    assertEquals(beta, api.getCodeChallenge("camelcasing"));
  }

  @Test
  void getRetiredChallenge() {
    assertEquals(retired, api.getCodeChallenge("calculator"));
  }

  @Test
  void getCodeChallengeWithHttpInfo() {
    ApiResponse<CodeChallenge> challengeResponse = api.getCodeChallengeWithHttpInfo("a-plus-b");
    assertEquals(200, challengeResponse.getStatusCode());
    assertEquals(trivial, challengeResponse.getData());
  }

  @Test
  void getNonexistentCodeChallenge() {
    ApiException error = assertThrows(ApiException.class, () -> api.getCodeChallenge("test-challenge-not-found"));
    assertEquals(404, error.getCode());
    assertEquals(
        "{\"success\":false,\"reason\":\"\\n" +
            "message:\\n  Document(s) not found for class CodeChallenge with id(s) test-challenge-not-found.\\n" +
            "summary:\\n  When calling CodeChallenge.find with an id or array of ids, each parameter must match a document in the database or this error will be raised. The search was for the id(s): test-challenge-not-found ... (1 total) and the following ids were not found: test-challenge-not-found.\\n"
            +
            "resolution:\\n  Search for an id that is in the database or set the Mongoid.raise_not_found_error configuration option to false, which will cause a nil to be returned instead of raising this error when searching for a single id, or only the matched documents when searching for multiples.\"}",
        error.getResponseBody());
  }

  @Test
  void getCodeChallengeWithNullParam() {
    ApiException error = assertThrows(ApiException.class, () -> api.getCodeChallenge(null));
    assertEquals(400, error.getCode());
    assertNull(error.getResponseBody());
    assertEquals("Missing the required parameter 'challenge' when calling getCodeChallenge", error.getMessage());
  }
}
