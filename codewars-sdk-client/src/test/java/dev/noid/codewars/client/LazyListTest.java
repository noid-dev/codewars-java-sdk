package dev.noid.codewars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class LazyListTest {

  @Test
  void load_first_two_pages() {
    List<String>[] pages = new List[] {
        List.of("a", "b", "c", "d", "e"),
        List.of("f", "g", "h", "i", "j"),
        List.of("k", "l", "m", "n", "o"),
        List.of("p", "q", "r", "s", "t")
    };

    var lazy = new LazyList<>(index -> pages[index], List.of(), 5, 10);
    assertEquals(List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"), lazy);
    assertEquals(10, lazy.size());
  }
}