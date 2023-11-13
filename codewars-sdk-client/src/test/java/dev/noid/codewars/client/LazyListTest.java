package dev.noid.codewars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class LazyListTest {

  private final List<String>[] pages = new List[] {
      List.of("a", "b", "c", "d", "e"),
      List.of("f", "g", "h", "i", "j"),
      List.of("k", "l", "m", "n", "o"),
      List.of("p", "q", "r", "s", "t"),
      List.of("u", "v", "w", "x", "y"),
      List.of("z")
  };

  @Test
  void load_first_two_pages() {
    var lazy = new LazyList<>(index -> pages[index], List.of(), 5, 10);
    assertEquals(List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"), lazy);
    assertEquals(10, lazy.size());
  }

  @Test
  void fetch_page_elements_by_index() {
    var lazy = new LazyList<>(index -> pages[index], List.of(), 5, 26);
    assertEquals("a", lazy.get(0));
    assertEquals("m", lazy.get(12));
    assertEquals("z", lazy.get(25));
  }
}