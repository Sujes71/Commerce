package es.sujes71.api.price.rest.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import es.sujes71.api.price.domain.model.PriceFilter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PriceFilterAdapterTest {

  @Test
  void adapt_ShouldReturnPriceFilterWithAllFields_WhenValidParametersProvided() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertEquals(brandId, result.getBrandId());
    assertEquals(productId, result.getProductId());
    assertEquals(applicationDate, result.getApplicationDate());
  }

  @Test
  void adapt_ShouldReturnPriceFilterWithNullBrandId_WhenBrandIdIsNull() {
    Integer brandId = null;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertNull(result.getBrandId());
    assertEquals(productId, result.getProductId());
    assertEquals(applicationDate, result.getApplicationDate());
  }

  @Test
  void adapt_ShouldReturnPriceFilterWithNullProductId_WhenProductIdIsNull() {
    Integer brandId = 1;
    Integer productId = null;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertEquals(brandId, result.getBrandId());
    assertNull(result.getProductId());
    assertEquals(applicationDate, result.getApplicationDate());
  }

  @Test
  void adapt_ShouldReturnPriceFilterWithNullApplicationDate_WhenApplicationDateIsNull() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = null;

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertEquals(brandId, result.getBrandId());
    assertEquals(productId, result.getProductId());
    assertNull(result.getApplicationDate());
  }

  @Test
  void adapt_ShouldReturnPriceFilterWithAllNullFields_WhenAllParametersAreNull() {
    Integer brandId = null;
    Integer productId = null;
    LocalDateTime applicationDate = null;

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertNull(result.getBrandId());
    assertNull(result.getProductId());
    assertNull(result.getApplicationDate());
  }

  @Test
  void adapt_ShouldHandleDifferentDateTimes_WhenDifferentApplicationDatesProvided() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertEquals(brandId, result.getBrandId());
    assertEquals(productId, result.getProductId());
    assertEquals(applicationDate, result.getApplicationDate());
  }

  @Test
  void adapt_ShouldHandleNegativeIds_WhenNegativeIdsProvided() {
    Integer brandId = -1;
    Integer productId = -35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    PriceFilter result = PriceFilterAdapter.adapt(brandId, productId, applicationDate);

    assertNotNull(result);
    assertEquals(brandId, result.getBrandId());
    assertEquals(productId, result.getProductId());
    assertEquals(applicationDate, result.getApplicationDate());
  }
}