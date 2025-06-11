package es.sujes71.shared.domain.model;

public record ErrorResponse(
    String code,
    String message,
    int status,
    long timestamp
) {

  public ErrorResponse(String code, String message, int status) {
    this(code, message, status, System.currentTimeMillis());
  }
}