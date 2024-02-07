package dev.noid.codewars.client;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class PageIteratorTest {

  @Test
  void iterate_through_fixed_page_size() {
    var pages = List.of("abcde", "fghij", "klmno", "pqrst", "uvwxy", "z");
    var iterator = new PageIterator<>(pages::get, p -> pages.size(), s -> asList(s.split("")));

    var items = new LinkedList<String>();
    while (iterator.hasNext()) {
      items.add(iterator.next());
    }

    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "z"
    ), items);
  }

  @Test
  void iterate_through_dynamic_page_size() {
    var pages = List.of("abc", "d", "efghi", "jk", "lmno", "p", "qrstuvwxy", "z");
    var iterator = new PageIterator<>(pages::get, p -> pages.size(), s -> asList(s.split("")));

    var items = new LinkedList<String>();
    while (iterator.hasNext()) {
      items.add(iterator.next());
    }

    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "z"
    ), items);
  }

  @Test
  void iterate_through_dynamic_page_count() {
    var pages = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    var iterator = new PageIterator<>(pages::remove, p -> pages.size(), List::of);

    var items = new LinkedList<Integer>();
    while (iterator.hasNext()) {
      items.add(iterator.next());
    }

    assertEquals(List.of(0, 2, 4, 6, 8), items);
  }

  @Test
  void iterate_through_first_two_pages() {
    var pages = List.of("abcde", "fghij", "klmno", "pqrst", "uvwxy", "z");
    var iterator = new PageIterator<>(pages::get, p -> 2, s -> asList(s.split("")));

    var items = new LinkedList<String>();
    while (iterator.hasNext()) {
      items.add(iterator.next());
    }

    assertEquals(List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"), items);
  }

  @Test
  void iterate_beyond_limits() {
    var iterator = new PageIterator<>(i -> List.of(), p -> 0, items -> items);
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  void unsupported_remove_operation() {
    var iterator = new PageIterator<>(i -> List.of(), p -> 0, items -> items);
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }
}