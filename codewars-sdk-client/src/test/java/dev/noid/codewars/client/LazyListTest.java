package dev.noid.codewars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

class LazyListTest {

  private final List<List<String>> pages = List.of(
      List.of("a", "b", "c", "d", "e"),
      List.of("f", "g", "h", "i", "j"),
      List.of("k", "l", "m", "n", "o"),
      List.of("p", "q", "r", "s", "t"),
      List.of("u", "v", "w", "x", "y"),
      List.of("z")
  );

  @Test
  void iterate_through_all_pages() {
    var pagination = new Pagination<>(pages::get, p -> pages.size(), items -> items);
    var loaded = new LinkedList<String>();
    for (String item : pagination) {
      loaded.add(item);
    }
    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "z"
    ), loaded);
  }
}