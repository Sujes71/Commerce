package es.sujes71.api.price.domain.model.exceptions;

public class PriceNotFoundException extends RuntimeException {
  public PriceNotFoundException(String message) {
    super(message);
  }
}
