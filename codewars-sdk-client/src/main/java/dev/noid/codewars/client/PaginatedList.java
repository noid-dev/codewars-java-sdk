package dev.noid.codewars.client;

import java.util.AbstractList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

class PaginatedList<E> extends AbstractList<E> {

  private final IntFunction<List<E>> pageLoader;
  private final List<E>[] cache;
  private final int totalItems;
  private final int pageSize;

  PaginatedList(IntFunction<List<E>> pageLoader, int totalPages, int totalItems, Map<Integer, List<E>> preloaded) {
    this.pageLoader = pageLoader;
    this.cache = new List[totalPages];
    preloaded.forEach((pageNumber, page) -> cache[pageNumber] = page);
    this.totalItems = totalItems;
    this.pageSize = (int) Math.ceil((double) totalItems / totalPages);
  }

  @Override
  public E get(int i) {
    if (i < 0 || i >= size()) {
      throw new IndexOutOfBoundsException();
    }

    int pageNumber = Math.floorDiv(i, pageSize);
    List<E> page = cache[pageNumber];
    if (page == null) {
      page = pageLoader.apply(pageNumber);
      cache[pageNumber] = page;
    }
    return page.get(i % pageSize);
  }

  @Override
  public int size() {
    return totalItems;
  }
}
