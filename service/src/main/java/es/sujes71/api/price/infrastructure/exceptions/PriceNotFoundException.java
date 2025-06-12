package es.sujes71.api.price.infrastructure.exceptions;

public class PriceNotFoundException extends RuntimeException {
  public PriceNotFoundException(String message) {
    super(message);
  }
}
