package es.sujes71.api.price.infrastructure.repository;

import static es.sujes71.api.price.domain.ports.outbound.PricePersistencePort.GET_PRICE_BY_PROPERTIES_ADDRESS;
import static es.sujes71.shared.domain.ports.outbound.OutboundPort.register;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.infrastructure.repository.h2.dao.PriceDao;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class PriceRepository {

  private static final Logger log = LogManager.getLogger(PriceRepository.class);

  private final PriceDao priceDao;

  public PriceRepository(PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  @PostConstruct
  public void start() {
    register(GET_PRICE_BY_PROPERTIES_ADDRESS, this::findAllByProperties);
  }

  public List<Price> findAllByProperties(PriceFilter filter) {
    try {
      List<PriceEntity> priceEntities = priceDao.findAllByProperties(
          filter.getBrandId(),
          filter.getProductId(),
          filter.getApplicationDate()
      );

      if (!priceEntities.isEmpty()) {
        log.info("Found {} prices for filter: {}", priceEntities.size(), filter);

        return priceEntities.stream()
            .map(PriceEntity::toDomain)
            .collect(Collectors.toList());
      }

      log.info("No prices found for filter: {}", filter);
      return List.of();
    } catch (Exception error) {
      log.error("Error finding prices: {}", error.getMessage());
      throw error;
    }
  }
}