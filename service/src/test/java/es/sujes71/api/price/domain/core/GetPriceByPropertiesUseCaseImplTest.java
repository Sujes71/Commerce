package es.sujes71.api.price.domain.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.domain.ports.outbound.PricePersistencePort;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GetPriceByPropertiesUseCaseImplTest {

  @Mock
  private PricePersistencePort pricePersistencePort;

  @InjectMocks
  private GetPriceByPropertiesUseCaseImpl getPriceByPropertiesUseCase;

  private PriceFilter priceFilter;
  private PriceEntity priceEntity;
  private Price expectedPrice;

  @BeforeEach
  void setUp() {
    priceFilter = PriceFilter.builder()
        .brandId(1)
        .productId(35455)
        .applicationDate(LocalDateTime.of(2020, 6, 14, 10, 0, 0))
        .build();

    priceEntity = PriceEntity.builder()
        .id(1L)
        .brandId(1)
        .productId(35455)
        .priceList(1)
        .startDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0))
        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
        .priority(0)
        .price(BigDecimal.valueOf(35.50))
        .currency("EUR")
        .build();

    expectedPrice = Price.builder()
        .brandId(1)
        .productId(35455)
        .priceList(1)
        .startDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0))
        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
        .price(BigDecimal.valueOf(35.50))
        .currency("EUR")
        .build();
  }

  @Test
  void execute_ShouldReturnPrice_WhenPriceEntityExists() {
    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.just(priceEntity));

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);
    Price actualPrice = result.block();

    assertNotNull(actualPrice);
    assertEquals(expectedPrice.getBrandId(), actualPrice.getBrandId());
    assertEquals(expectedPrice.getProductId(), actualPrice.getProductId());
    assertEquals(expectedPrice.getPriceList(), actualPrice.getPriceList());
    assertEquals(expectedPrice.getStartDate(), actualPrice.getStartDate());
    assertEquals(expectedPrice.getEndDate(), actualPrice.getEndDate());
    assertEquals(expectedPrice.getPrice(), actualPrice.getPrice());
    assertEquals(expectedPrice.getCurrency(), actualPrice.getCurrency());

    verify(pricePersistencePort).getPriceByProperties(priceFilter);
  }

  @Test
  void execute_ShouldThrowRuntimeException_WhenPriceNotFound() {
    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.empty());

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> result.block());
    assertEquals("Price not found for the given properties.", exception.getMessage());

    verify(pricePersistencePort).getPriceByProperties(priceFilter);
  }

  @Test
  void execute_ShouldPropagateException_WhenPersistencePortThrowsException() {
    RuntimeException persistenceException = new RuntimeException("Database connection failed");
    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.error(persistenceException));

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> result.block());
    assertEquals("Database connection failed", exception.getMessage());

    verify(pricePersistencePort).getPriceByProperties(priceFilter);
  }

  @Test
  void execute_ShouldCallPersistencePortWithCorrectFilter_WhenExecuted() {
    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.just(priceEntity));

    getPriceByPropertiesUseCase.execute(priceFilter);

    verify(pricePersistencePort).getPriceByProperties(priceFilter);
  }

  @Test
  void execute_ShouldHandleNullFilter_WhenFilterIsNull() {
    when(pricePersistencePort.getPriceByProperties(null))
        .thenReturn(Mono.empty());

    Mono<Price> result = getPriceByPropertiesUseCase.execute(null);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> result.block());
    assertEquals("Price not found for the given properties.", exception.getMessage());

    verify(pricePersistencePort).getPriceByProperties(null);
  }

  @Test
  void execute_ShouldMapEntityToDomainCorrectly_WhenEntityExists() {
    PriceEntity customEntity = PriceEntity.builder()
        .id(2L)
        .brandId(2)
        .productId(12345)
        .priceList(2)
        .startDate(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
        .endDate(LocalDateTime.of(2021, 12, 31, 23, 59, 59))
        .priority(1)
        .price(BigDecimal.valueOf(99.99))
        .currency("USD")
        .build();

    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.just(customEntity));

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);
    Price actualPrice = result.block();

    assertNotNull(actualPrice);
    assertEquals(2, actualPrice.getBrandId());
    assertEquals(12345, actualPrice.getProductId());
    assertEquals(2, actualPrice.getPriceList());
    assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0, 0), actualPrice.getStartDate());
    assertEquals(LocalDateTime.of(2021, 12, 31, 23, 59, 59), actualPrice.getEndDate());
    assertEquals(BigDecimal.valueOf(99.99), actualPrice.getPrice());
    assertEquals("USD", actualPrice.getCurrency());

    verify(pricePersistencePort).getPriceByProperties(priceFilter);
  }

  @Test
  void execute_ShouldReturnMonoPrice_WhenCalled() {
    when(pricePersistencePort.getPriceByProperties(any(PriceFilter.class)))
        .thenReturn(Mono.just(priceEntity));

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);

    assertNotNull(result);
    assertInstanceOf(Mono.class, result);

    Price actualPrice = result.block();
    assertNotNull(actualPrice);
    assertEquals(Price.class, actualPrice.getClass());
  }

  @Test
  void execute_ShouldUseCorrectErrorMessage_WhenPriceNotFound() {
    when(pricePersistencePort.getPriceByProperties(priceFilter))
        .thenReturn(Mono.empty());

    Mono<Price> result = getPriceByPropertiesUseCase.execute(priceFilter);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> result.block());
    assertEquals("Price not found for the given properties.", exception.getMessage());
    assertEquals(RuntimeException.class, exception.getClass());
  }

  @Test
  void constructor_ShouldCreateInstance_WhenPersistencePortProvided() {
    GetPriceByPropertiesUseCaseImpl useCase = new GetPriceByPropertiesUseCaseImpl(pricePersistencePort);

    assertNotNull(useCase);
  }
}