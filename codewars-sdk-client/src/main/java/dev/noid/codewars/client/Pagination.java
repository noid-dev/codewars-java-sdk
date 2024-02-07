package dev.noid.codewars.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

class Pagination<P, E> implements Iterable<E> {

  private final IntFunction<P> pageFetcher;
  private final ToIntFunction<P> indexRefresher;
  private final Function<P, Collection<E>> dataExtractor;

  Pagination(IntFunction<P> pageFetcher, ToIntFunction<P> indexRefresher, Function<P, Collection<E>> dataExtractor) {
    this.pageFetcher = pageFetcher;
    this.indexRefresher = indexRefresher;
    this.dataExtractor = dataExtractor;
  }

  @Override
  public Iterator<E> iterator() {
    return new PageIterator<>(pageFetcher, indexRefresher, dataExtractor);
  }
}
