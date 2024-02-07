package dev.noid.codewars.sample;

import dev.noid.codewars.client.CodewarsClient;
import dev.noid.codewars.client.model.CompletedChallenge;
import java.util.Iterator;

public class Main {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Username or ID is required.");
      System.exit(1);
    }
    String username = args[0];
    CodewarsClient codewars = CodewarsClient.getDefault();
    System.out.printf("User warrior rank: %s%n", codewars.getUser(username).getRanks().getOverall().getName());
    System.out.printf("Completed challenges: %d%n", codewars.listCompletedChallenges(username).size());
    System.out.printf("Authored challenged: %d%n", codewars.listAuthoredChallenges(username).size());
    System.out.printf("Last solved challenge: %s%n", findLastSolvedChallenge(codewars, username));
  }

  private static String findLastSolvedChallenge(CodewarsClient codewars, String username) {
    // sorted by completion date, from most recent to oldest.
    Iterable<CompletedChallenge> completed = codewars.listCompletedChallenges(username);
    Iterator<CompletedChallenge> challengeIterator = completed.iterator();
    if (challengeIterator.hasNext()) {
      return "<Nan>";
    }
    String recentSolvedId = challengeIterator.next().getId();
    return codewars.getCodeChallenge(recentSolvedId).getUrl();
  }
}
