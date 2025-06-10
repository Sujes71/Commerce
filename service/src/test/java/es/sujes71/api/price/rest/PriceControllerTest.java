package es.sujes71.api.price.rest;

import static es.sujes71.api.price.rest.adapter.PriceFilterAdapter.adapt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceController Tests")
class PriceControllerTest {

  @Mock
  private GetPriceByPropertiesUseCase getPriceByPropertiesUseCase;

  @InjectMocks
  private PriceController priceController;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(getPriceByPropertiesUseCase);
  }

  @Test
  @DisplayName("Should return price when valid parameters are provided")
  void shouldReturnPriceWhenValidParametersProvided() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    Price expectedPrice = Price.builder()
        .brandId(brandId)
        .productId(productId)
        .priceList(1)
        .startDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0))
        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
        .price(new BigDecimal("35.50"))
        .currency("EUR")
        .build();

    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.just(expectedPrice));

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    Price actualPrice = result.block();
    assertThat(actualPrice).isNotNull();
    assertThat(actualPrice.getBrandId()).isEqualTo(brandId);
    assertThat(actualPrice.getProductId()).isEqualTo(productId);
    assertThat(actualPrice.getPriceList()).isEqualTo(1);
    assertThat(actualPrice.getPrice()).isEqualTo(new BigDecimal("35.50"));
    assertThat(actualPrice.getCurrency()).isEqualTo("EUR");
    assertThat(actualPrice.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0, 0));
    assertThat(actualPrice.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should propagate error when use case fails")
  void shouldPropagateErrorWhenUseCaseFails() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    RuntimeException expectedException = new RuntimeException("Price not found");
    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.error(expectedException));

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    assertThatThrownBy(result::block)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Price not found");

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should return empty Mono when price not found")
  void shouldReturnEmptyMonoWhenPriceNotFound() {
    Integer brandId = 999;
    Integer productId = 99999;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.empty());

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    Price actualPrice = result.block();
    assertThat(actualPrice).isNull();

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should handle different exception types from use case")
  void shouldHandleDifferentExceptionTypesFromUseCase() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    IllegalArgumentException expectedException = new IllegalArgumentException("Invalid parameters");
    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.error(expectedException));

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    assertThatThrownBy(result::block)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid parameters");

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should handle boundary values")
  void shouldHandleBoundaryValues() {
    Integer brandId = Integer.MAX_VALUE;
    Integer productId = Integer.MIN_VALUE;
    LocalDateTime applicationDate = LocalDateTime.of(1999, 1, 1, 0, 0, 0);

    Price expectedPrice = Price.builder()
        .brandId(brandId)
        .productId(productId)
        .priceList(999)
        .price(new BigDecimal("0.01"))
        .currency("USD")
        .build();

    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.just(expectedPrice));

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    Price actualPrice = result.block();
    assertThat(actualPrice).isNotNull();
    assertThat(actualPrice.getBrandId()).isEqualTo(brandId);
    assertThat(actualPrice.getProductId()).isEqualTo(productId);
    assertThat(actualPrice.getPrice()).isEqualTo(new BigDecimal("0.01"));

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should handle different date precisions")
  void shouldHandleDifferentDatePrecisions() {
    Integer brandId = 2;
    Integer productId = 12345;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 15, 30, 45, 123456789);

    Price expectedPrice = Price.builder()
        .brandId(brandId)
        .productId(productId)
        .priceList(2)
        .price(new BigDecimal("99.99"))
        .currency("EUR")
        .build();

    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.just(expectedPrice));

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    Price actualPrice = result.block();
    assertThat(actualPrice).isNotNull();
    assertThat(actualPrice.getBrandId()).isEqualTo(brandId);
    assertThat(actualPrice.getProductId()).isEqualTo(productId);

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should verify adapter is called with correct parameters")
  void shouldVerifyAdapterIsCalledWithCorrectParameters() {
    Integer brandId = 5;
    Integer productId = 98765;
    LocalDateTime applicationDate = LocalDateTime.of(2023, 12, 25, 12, 0, 0);

    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(Mono.empty());

    priceController.getPrice(brandId, productId, applicationDate);

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }

  @Test
  @DisplayName("Should return same Mono from use case")
  void shouldReturnSameMonoFromUseCase() {
    Integer brandId = 1;
    Integer productId = 35455;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    Price price = Price.builder()
        .brandId(brandId)
        .productId(productId)
        .build();

    Mono<Price> expectedMono = Mono.just(price);
    when(getPriceByPropertiesUseCase.execute(any()))
        .thenReturn(expectedMono);

    Mono<Price> result = priceController.getPrice(brandId, productId, applicationDate);

    assertThat(result).isSameAs(expectedMono);

    verify(getPriceByPropertiesUseCase).execute(adapt(brandId, productId, applicationDate));
  }
}