package dev.noid.codewars.client;

import dev.noid.codewars.client.api.CodeChallengesApi;
import dev.noid.codewars.client.api.UsersApi;
import dev.noid.codewars.client.model.AuthoredChallenge;
import dev.noid.codewars.client.model.CodeChallenge;
import dev.noid.codewars.client.model.CompletedChallenge;
import dev.noid.codewars.client.model.CompletedChallenges;
import dev.noid.codewars.client.model.User;
import java.util.List;
import java.util.function.IntFunction;

public final class CodewarsClient {

  private static CodewarsClient defaultClient;

  public static CodewarsClient getDefault() {
    synchronized (CodewarsClient.class) {
      if (defaultClient == null) {
        defaultClient = new CodewarsClient(Configuration.getDefaultApiClient());
      }
    }
    return defaultClient;
  }

  private final UsersApi usersApi;
  private final CodeChallengesApi challengesApi;

  CodewarsClient(ApiClient apiClient) {
    this.usersApi = new UsersApi(apiClient);
    this.challengesApi = new CodeChallengesApi(apiClient);
  }

  public User getUser(String idOrUsername) throws ApiException {
    return usersApi.getUser(idOrUsername);
  }

  public List<AuthoredChallenge> listAuthoredChallenges(String idOrUsername) throws ApiException {
    return usersApi.listAuthoredChallenges(idOrUsername).getData();
  }

  public List<CompletedChallenge> listCompletedChallenges(String idOrUsername) throws ApiException {
    CompletedChallenges firstPage = usersApi.listCompletedChallenges(idOrUsername, 0);
    List<CompletedChallenge> recentChallenges = firstPage.getData();
    IntFunction<List<CompletedChallenge>> pageLoader = page -> usersApi.listCompletedChallenges(idOrUsername, page).getData();
    return new LazyList<>(pageLoader, recentChallenges, recentChallenges.size(), firstPage.getTotalItems());
  }

  public CodeChallenge getCodeChallenge(String idOrSlag) throws ApiException {
    return challengesApi.getCodeChallenge(idOrSlag);
  }
}
