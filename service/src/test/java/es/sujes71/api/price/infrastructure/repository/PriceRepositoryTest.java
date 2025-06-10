package es.sujes71.api.price.infrastructure.repository;

import static es.sujes71.api.price.domain.ports.outbound.PricePersistencePort.GET_PRICE_BY_PROPERTIES_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.infrastructure.repository.h2.dao.PriceDao;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import es.sujes71.shared.domain.ports.outbound.OutboundPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryTest {

  @Mock
  private PriceDao priceDao;

  @InjectMocks
  private PriceRepository priceRepository;

  private PriceFilter priceFilter;
  private PriceEntity priceEntity;

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
  }

  @Test
  void start_ShouldRegisterWithOutboundPort() {
    try (MockedStatic<OutboundPort> mockedOutboundPort = mockStatic(OutboundPort.class)) {

      priceRepository.start();

      mockedOutboundPort.verify(() ->
          OutboundPort.register(eq(GET_PRICE_BY_PROPERTIES_ADDRESS), any())
      );
    }
  }

  @Test
  void findByProperties_ShouldReturnPriceEntity_WhenPriceExists() {
    when(priceDao.findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    )).thenReturn(Mono.just(priceEntity));

    Mono<PriceEntity> result = priceRepository.findByProperties(priceFilter);
    PriceEntity actualEntity = result.block();

    assertEquals(priceEntity, actualEntity);
    verify(priceDao).findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    );
  }

  @Test
  void findByProperties_ShouldReturnEmpty_WhenPriceNotExists() {
    when(priceDao.findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    )).thenReturn(Mono.empty());

    Mono<PriceEntity> result = priceRepository.findByProperties(priceFilter);
    PriceEntity actualEntity = result.block();

    assertNull(actualEntity);
    verify(priceDao).findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    );
  }

  @Test
  void findByProperties_ShouldPropagateError_WhenDaoThrowsException() {
    RuntimeException exception = new RuntimeException("Database connection failed");
    when(priceDao.findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    )).thenReturn(Mono.error(exception));

    Mono<PriceEntity> result = priceRepository.findByProperties(priceFilter);

    assertThrows(RuntimeException.class, result::block);
    verify(priceDao).findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    );
  }

  @Test
  void findByProperties_ShouldHandleNullFields_WhenFilterHasNullValues() {
    PriceFilter filterWithNulls = PriceFilter.builder()
        .brandId(null)
        .productId(null)
        .applicationDate(null)
        .build();

    when(priceDao.findByProperties(null, null, null))
        .thenReturn(Mono.empty());

    Mono<PriceEntity> result = priceRepository.findByProperties(filterWithNulls);
    PriceEntity actualEntity = result.block();

    assertNull(actualEntity);
    verify(priceDao).findByProperties(null, null, null);
  }

  @Test
  void findByProperties_ShouldCallDaoWithCorrectParameters_WhenValidFilterProvided() {
    Integer expectedBrandId = 2;
    Integer expectedProductId = 12345;
    LocalDateTime expectedDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    PriceFilter customFilter = PriceFilter.builder()
        .brandId(expectedBrandId)
        .productId(expectedProductId)
        .applicationDate(expectedDate)
        .build();

    when(priceDao.findByProperties(expectedBrandId, expectedProductId, expectedDate))
        .thenReturn(Mono.just(priceEntity));

    priceRepository.findByProperties(customFilter);

    verify(priceDao).findByProperties(expectedBrandId, expectedProductId, expectedDate);
  }

  @Test
  void findByProperties_ShouldLogSuccess_WhenPriceIsFound() {
    when(priceDao.findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    )).thenReturn(Mono.just(priceEntity));

    Mono<PriceEntity> result = priceRepository.findByProperties(priceFilter);
    PriceEntity actualEntity = result.block();

    assertEquals(priceEntity, actualEntity);
  }

  @Test
  void findByProperties_ShouldLogError_WhenErrorOccurs() {
    RuntimeException exception = new RuntimeException("Test error");
    when(priceDao.findByProperties(
        priceFilter.getBrandId(),
        priceFilter.getProductId(),
        priceFilter.getApplicationDate()
    )).thenReturn(Mono.error(exception));

    Mono<PriceEntity> result = priceRepository.findByProperties(priceFilter);

    assertThrows(RuntimeException.class, result::block);

  }
}