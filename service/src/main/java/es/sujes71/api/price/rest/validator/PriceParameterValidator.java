package es.sujes71.api.price.rest.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class PriceParameterValidator {

  public static void validate(String brandId, String productId, String applicationDate) {
    if (brandId == null || brandId.trim().isEmpty()) {
      throw new IllegalArgumentException("brandId is required");
    }

    try {
      int parsedBrandId = Integer.parseInt(brandId.trim());
      if (parsedBrandId <= 0) {
        throw new IllegalArgumentException("brandId must be a positive integer");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("brandId must be a valid integer");
    }

    if (productId == null || productId.trim().isEmpty()) {
      throw new IllegalArgumentException("productId is required");
    }

    try {
      int parsedProductId = Integer.parseInt(productId.trim());
      if (parsedProductId <= 0) {
        throw new IllegalArgumentException("productId must be a positive integer");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("productId must be a valid integer");
    }

    try {
      int parsedProductId = Integer.parseInt(productId.trim());
      if (parsedProductId <= 0) {
        throw new IllegalArgumentException("productId must be a positive integer");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("productId must be a valid integer");
    }

    if (applicationDate == null || applicationDate.trim().isEmpty()) {
      throw new IllegalArgumentException("applicationDate is required");
    }

    try {
      LocalDateTime.parse(applicationDate.trim());
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("applicationDate must be in ISO format (yyyy-MM-ddTHH:mm:ss)");
    }
  }
}