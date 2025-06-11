package es.sujes71.api.price.domain.core;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.domain.model.exceptions.PriceNotFoundException;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import es.sujes71.api.price.domain.ports.outbound.PricePersistencePort;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetPriceByPropertiesUseCaseImpl implements GetPriceByPropertiesUseCase {

  private final PricePersistencePort pricePersistencePort;

  public GetPriceByPropertiesUseCaseImpl(PricePersistencePort pricePersistencePort) {
    this.pricePersistencePort = pricePersistencePort;
  }

  @Override
  public Price execute(PriceFilter input) {
    List<PriceEntity> priceEntities = pricePersistencePort.getAllPricesByProperties(input);

    if (priceEntities.isEmpty()) {
      throw new PriceNotFoundException("No prices found for the given properties.");
    }

    PriceEntity selectedPrice = priceEntities.stream()
        .max(Comparator.comparingInt(PriceEntity::getPriority))
        .orElseThrow();

    return selectedPrice.toDomain();
  }
}