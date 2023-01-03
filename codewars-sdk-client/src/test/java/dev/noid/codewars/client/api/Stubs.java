package dev.noid.codewars.client.api;

import dev.noid.codewars.client.JSON;

public class Stubs {

  public static <T> T read(String name, Class<T> type) {
    try (var stream = Stubs.class.getClassLoader().getResourceAsStream(name)) {
      return JSON.getDefault().getMapper().readValue(stream, type);
    } catch (Exception cause) {
      throw new RuntimeException(cause);
    }
  }
}
