package es.sujes71.api.price.domain.core;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import es.sujes71.api.price.domain.ports.outbound.PricePersistencePort;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetPriceByPropertiesUseCaseImpl implements GetPriceByPropertiesUseCase {

  private final PricePersistencePort pricePersistencePort;

  public GetPriceByPropertiesUseCaseImpl(PricePersistencePort pricePersistencePort) {
    this.pricePersistencePort = pricePersistencePort;
  }

  @Override
  public Mono<Price> execute(PriceFilter input) {
    return pricePersistencePort.getPriceByProperties(input)
        .map(PriceEntity::toDomain)
        .switchIfEmpty(Mono.error(new RuntimeException("Price not found for the given properties.")));
  }
}
