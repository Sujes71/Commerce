package es.sujes71.api.price.domain.ports.inbound;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.shared.domain.ports.inbound.UseCase;

public interface GetPriceByPropertiesUseCase extends UseCase<PriceFilter, Price> {
}
