package dev.noid.codewars.client;

import dev.noid.codewars.client.api.CodeChallengesApi;
import dev.noid.codewars.client.api.UsersApi;
import dev.noid.codewars.client.model.AuthoredChallenge;
import dev.noid.codewars.client.model.CodeChallenge;
import dev.noid.codewars.client.model.CompletedChallenge;
import dev.noid.codewars.client.model.CompletedChallenges;
import dev.noid.codewars.client.model.User;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

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

  public Collection<AuthoredChallenge> listAuthoredChallenges(String idOrUsername) throws ApiException {
    return usersApi.listAuthoredChallenges(idOrUsername).getData();
  }

  public Iterable<CompletedChallenge> listCompletedChallenges(String idOrUsername) throws ApiException {
    IntFunction<CompletedChallenges> pageFetcher = pageIndex -> usersApi.listCompletedChallenges(idOrUsername, pageIndex);
    ToIntFunction<CompletedChallenges> indexRefresher = CompletedChallenges::getTotalPages;
    Function<CompletedChallenges, Collection<CompletedChallenge>> dataExtractor = CompletedChallenges::getData;
    return new Pagination<>(pageFetcher, indexRefresher, dataExtractor);
  }

  public CodeChallenge getCodeChallenge(String idOrSlag) throws ApiException {
    return challengesApi.getCodeChallenge(idOrSlag);
  }
}
