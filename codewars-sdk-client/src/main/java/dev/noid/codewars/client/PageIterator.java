package dev.noid.codewars.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

class PageIterator<P, E> implements Iterator<E> {

  private final IntFunction<P> pageFetcher;
  private final ToIntFunction<P> indexRefresher;
  private final Function<P, Collection<E>> dataExtractor;
  private final LinkedList<E> recentPageItems = new LinkedList<>();
  private int pageIndex;
  private int totalPages = -1;

  PageIterator(IntFunction<P> pageFetcher, ToIntFunction<P> indexRefresher, Function<P, Collection<E>> dataExtractor) {
    this.pageFetcher = pageFetcher;
    this.indexRefresher = indexRefresher;
    this.dataExtractor = dataExtractor;
  }

  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    if (recentPageItems.isEmpty()) {
      loadNextPageItems();
    }
    return recentPageItems.removeFirst();
  }

  @Override
  public boolean hasNext() {
    if (totalPages < 0) {
      try {
        loadNextPageItems();
      } catch (Exception failed) {
        return false;
      }
    }
    return pageIndex < totalPages || !recentPageItems.isEmpty();
  }

  private void loadNextPageItems() {
    P page = pageFetcher.apply(pageIndex);
    if (page == null) {
      throw new IllegalStateException("Unable to load page #" + pageIndex);
    }

    Collection<E> items = dataExtractor.apply(page);
    if (items == null) {
      throw new IllegalStateException("Unable to extract data from page #" + pageIndex);
    }

    int pagesAvailable = indexRefresher.applyAsInt(page);
    totalPages = Math.max(pagesAvailable, 0);

    if (!items.isEmpty()) {
      recentPageItems.addAll(items);
    }
    pageIndex++;
  }
}