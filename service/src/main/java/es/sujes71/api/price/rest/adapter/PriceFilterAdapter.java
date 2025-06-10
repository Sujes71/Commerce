package es.sujes71.api.price.rest.adapter;


import es.sujes71.api.price.domain.model.PriceFilter;
import java.time.LocalDateTime;

public class PriceFilterAdapter {

  public static PriceFilter adapt(Integer brandId, Integer productId, LocalDateTime applicationDate) {
    return PriceFilter.builder()
        .brandId(brandId)
        .productId(productId)
        .applicationDate(applicationDate)
        .build();
  }
}