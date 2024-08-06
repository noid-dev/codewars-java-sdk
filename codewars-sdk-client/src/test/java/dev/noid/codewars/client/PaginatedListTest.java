package dev.noid.codewars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaginatedListTest {

  private final List<List<String>> remotePages = Mockito.spy(List.of(
      List.of("a", "b", "c", "d", "e"),
      List.of("f", "g", "h", "i", "j"),
      List.of("k", "l", "m", "n", "o"),
      List.of("p", "q", "r", "s", "t"),
      List.of("u", "v", "w", "x", "y"),
      List.of("z")
  ));

  @Test
  void load_all_remote_pages() {
    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "z"
    ), getList(6, 26, Map.of()));

    Mockito.verify(remotePages, Mockito.times(remotePages.size())).get(anyInt());
    for (int page = 0; page < remotePages.size(); page++) {
      Mockito.verify(remotePages, Mockito.times(1)).get(page);
    }
  }

  @Test
  void preload_first_page() {
    assertEquals(List.of(
        "1", "2", "3", "4", "5",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "z"
    ), getList(6, 26, Map.of(0, List.of("1", "2", "3", "4", "5"))));

    Mockito.verify(remotePages, Mockito.times(remotePages.size() - 1)).get(anyInt());
    for (int page = 1; page < remotePages.size(); page++) {
      Mockito.verify(remotePages, Mockito.times(1)).get(page);
    }
  }

  @Test
  void preload_last_page() {
    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y",
        "1"
    ), getList(6, 26, Map.of(5, List.of("1", "2", "3", "4", "5"))));

    Mockito.verify(remotePages, Mockito.times(remotePages.size() - 1)).get(anyInt());
    for (int page = 0; page < remotePages.size() - 1; page++) {
      Mockito.verify(remotePages, Mockito.times(1)).get(page);
    }
  }

  @Test
  void load_no_remote_pages() {
    Map<Integer, List<String>> preloaded = Map.of(
        0, List.of("1", "2", "3", "4", "5"),
        1, List.of("6", "7", "8", "9", "0"),
        2, List.of("~", "!", "@", "#", "$"),
        3, List.of("%", "^", "&", "*", "("),
        4, List.of(")", "_", "+", "`", "-"),
        5, List.of("=")
    );

    assertEquals(List.of(
        "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "0",
        "~", "!", "@", "#", "$",
        "%", "^", "&", "*", "(",
        ")", "_", "+", "`", "-",
        "="
    ), getList(6, 26, preloaded));

    Mockito.verify(remotePages, Mockito.times(0)).get(anyInt());
  }

  @Test
  void limit_pages_access() {
    assertEquals(List.of(
        "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j",
        "k", "l", "m"
    ), getList(3, 13, Map.of()));

    Mockito.verify(remotePages, Mockito.times(3)).get(anyInt());
    Mockito.verify(remotePages, Mockito.times(1)).get(0);
    Mockito.verify(remotePages, Mockito.times(1)).get(1);
    Mockito.verify(remotePages, Mockito.times(1)).get(2);
  }

  @Test
  void read_from_empty() {
    List<String> list = getList(0, 0, Map.of());
    assertTrue(list.isEmpty());
    assertEquals(List.of(), list);
  }

  @Test
  void constant_size() {
    List<String> list = getList(6, 26, Map.of());
    assertEquals(26, list.size());
    for (String ignored : list) {
      assertEquals(26, list.size());
    }
    assertEquals(26, list.size());
  }

  @Test
  void immutability() {
    List<String> list = getList(1, 1, Map.of());
    assertThrows(UnsupportedOperationException.class, () -> list.add(0, "!"));
    assertThrows(UnsupportedOperationException.class, () -> list.set(0, "!"));
    assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
    assertThrows(UnsupportedOperationException.class, () -> list.remove("a"));
  }

  private List<String> getList(int totalPages, int totalItems, Map<Integer, List<String>> preloaded) {
    return new PaginatedList<>(remotePages::get, totalPages, totalItems, preloaded);
  }
}