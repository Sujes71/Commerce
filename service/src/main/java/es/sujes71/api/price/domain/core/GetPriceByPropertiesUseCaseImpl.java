package es.sujes71.api.price.domain.core;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.domain.model.exceptions.PriceNotFoundException;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import es.sujes71.api.price.domain.ports.outbound.PricePersistencePort;
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
    List<Price> prices = pricePersistencePort.getAllPricesByProperties(input);

    if (prices.isEmpty()) {
      throw new PriceNotFoundException();
    }

    return prices.stream()
        .max(Comparator.comparingInt(Price::getPriority))
        .orElseThrow();
  }
}