package es.sujes71.api.price.domain.ports.outbound;

import static es.sujes71.api.price.domain.ports.outbound.PricePersistencePort.GET_PRICE_BY_PROPERTIES_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mockStatic;

import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import es.sujes71.shared.domain.model.Message;
import es.sujes71.shared.domain.ports.outbound.OutboundPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PricePersistencePortTest {

  private PricePersistencePort pricePersistencePort;
  private PriceFilter priceFilter;
  private PriceEntity priceEntity;

  @BeforeEach
  void setUp() {
    pricePersistencePort = new PricePersistencePort();

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
  }

  @Test
  void getPriceByProperties_ShouldReturnPriceEntity_WhenHandlerExists() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.just(priceEntity));

      Mono<PriceEntity> result = pricePersistencePort.getPriceByProperties(priceFilter);
      PriceEntity actualEntity = result.block();

      assertNotNull(actualEntity);
      assertEquals(priceEntity, actualEntity);

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              GET_PRICE_BY_PROPERTIES_ADDRESS.equals(message.address()) &&
                  priceFilter.equals(message.body())
          ))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldReturnEmpty_WhenNoDataFound() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.empty());

      Mono<PriceEntity> result = pricePersistencePort.getPriceByProperties(priceFilter);
      PriceEntity actualEntity = result.block();

      assertNull(actualEntity);

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              GET_PRICE_BY_PROPERTIES_ADDRESS.equals(message.address()) &&
                  priceFilter.equals(message.body())
          ))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldPropagateError_WhenHandlerThrowsException() {
    RuntimeException exception = new RuntimeException("Handler execution failed");
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.error(exception));

      Mono<PriceEntity> result = pricePersistencePort.getPriceByProperties(priceFilter);

      RuntimeException thrownException = assertThrows(RuntimeException.class, result::block);
      assertEquals("Handler execution failed", thrownException.getMessage());

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              GET_PRICE_BY_PROPERTIES_ADDRESS.equals(message.address()) &&
                  priceFilter.equals(message.body())
          ))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldPropagateError_WhenNoHandlerRegistered() {
    IllegalArgumentException exception = new IllegalArgumentException("No handler found for address: " + GET_PRICE_BY_PROPERTIES_ADDRESS);
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.error(exception));

      Mono<PriceEntity> result = pricePersistencePort.getPriceByProperties(priceFilter);

      IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, result::block);
      assertEquals("No handler found for address: " + GET_PRICE_BY_PROPERTIES_ADDRESS, thrownException.getMessage());

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              GET_PRICE_BY_PROPERTIES_ADDRESS.equals(message.address()) &&
                  priceFilter.equals(message.body())
          ))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldCreateCorrectMessage_WhenCalled() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.just(priceEntity));

      pricePersistencePort.getPriceByProperties(priceFilter);

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message -> {
            assertEquals(GET_PRICE_BY_PROPERTIES_ADDRESS, message.address());
            assertEquals(priceFilter, message.body());
            assertEquals(priceFilter.getBrandId(), ((PriceFilter) message.body()).getBrandId());
            assertEquals(priceFilter.getProductId(), ((PriceFilter) message.body()).getProductId());
            assertEquals(priceFilter.getApplicationDate(), ((PriceFilter) message.body()).getApplicationDate());
            return true;
          }))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldHandleNullFilter_WhenFilterIsNull() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.empty());

      Mono<PriceEntity> result = pricePersistencePort.getPriceByProperties(null);
      PriceEntity actualEntity = result.block();

      assertNull(actualEntity);

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              GET_PRICE_BY_PROPERTIES_ADDRESS.equals(message.address()) &&
                  message.body() == null
          ))
      );
    }
  }

  @Test
  void getPriceByProperties_ShouldUseCorrectAddress_WhenCalled() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {
      mockedOutboundPort.when(() -> OutboundPort.requestEvent(any(Message.class)))
          .thenReturn(Mono.just(priceEntity));

      pricePersistencePort.getPriceByProperties(priceFilter);

      mockedOutboundPort.verify(() ->
          OutboundPort.requestEvent(argThat(message ->
              "getPriceByPropertiesAddress".equals(message.address())
          ))
      );
    }
  }

  @Test
  void constantValue_ShouldHaveCorrectAddress() {
    assertEquals("getPriceByPropertiesAddress", GET_PRICE_BY_PROPERTIES_ADDRESS);
  }
}