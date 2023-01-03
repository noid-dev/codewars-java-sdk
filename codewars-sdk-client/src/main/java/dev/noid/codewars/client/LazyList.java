package dev.noid.codewars.client;

import java.util.AbstractList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class LazyList<E> extends AbstractList<E> {

  private final Map<Integer, E> loaded = new LinkedHashMap<>();
  private final IntFunction<List<E>> pageLoader;
  private final int totalItems;
  private final int pageSize;

  /**
   * Create a LazyList backed by the given query, using pageSize results per page, and expecting numResults from the
   * query.
   */
  public LazyList(IntFunction<List<E>> pageLoader, List<E> preload, int pageSize, int totalItems) {
    this.pageLoader = pageLoader;
    this.pageSize = pageSize;
    this.totalItems = totalItems;
    addItems(0, preload);
  }

  /**
   * Fetch an item, loading it from the query results if it hasn't already been.
   */
  @Override
  public E get(int i) {
    if (i < 0 || i >= size()) {
      throw new IllegalArgumentException(i + " is not valid index.");
    }
    if (!loaded.containsKey(i)) {
      List<E> page = pageLoader.apply(i / pageSize);
      addItems(i, page);
    }
    return loaded.get(i);
  }

  @Override
  public int size() {
    return totalItems;
  }

  private void addItems(int offset, List<E> items) {
    for (int i = 0; i < items.size(); i++) {
      loaded.put(offset + i, items.get(i));
    }
  }
}