package es.sujes71.api.price.infrastructure.repository;

import static es.sujes71.api.price.domain.ports.outbound.PricePersistencePort.GET_PRICE_BY_PROPERTIES_ADDRESS;
import static es.sujes71.shared.domain.ports.outbound.OutboundPort.register;

import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.infrastructure.repository.h2.dao.PriceDao;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PriceRepository {

	private static final Logger log = LogManager.getLogger(PriceRepository.class);

	private final PriceDao priceDao;

  public PriceRepository(PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  @PostConstruct
	public void start() {
    register(GET_PRICE_BY_PROPERTIES_ADDRESS, this::findByProperties);
	}

	public Mono<PriceEntity> findByProperties(PriceFilter filter) {
		return priceDao.findByProperties(filter.getBrandId(), filter.getProductId(), filter.getApplicationDate())
			.doOnSuccess(price -> {
				if (Objects.nonNull(price)) {
					log.info("Found {}", price);
				}
			})
			.doOnError(error -> log.error("Error finding price: {}", error.getMessage()));
	}
}