package es.sujes71.api.price.infrastructure.exceptions;

public class PriceNotFoundException extends RuntimeException {
  public PriceNotFoundException() {
    super("No prices found for the given properties.");
  }
}
