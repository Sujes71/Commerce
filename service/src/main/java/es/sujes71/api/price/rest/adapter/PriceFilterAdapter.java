package es.sujes71.api.price.rest.adapter;


import es.sujes71.api.price.domain.model.PriceFilter;
import java.time.LocalDateTime;

public class PriceFilterAdapter {

  public static PriceFilter adapt(String brandId, String productId, String applicationDate) {
    return PriceFilter.builder()
        .brandId(Integer.parseInt(brandId.trim()))
        .productId(Integer.parseInt(productId.trim()))
        .applicationDate(LocalDateTime.parse(applicationDate.trim()))
        .build();
  }
}