package es.sujes71.api.price.domain.ports.outbound;

import static es.sujes71.shared.domain.ports.outbound.OutboundPort.requestEvent;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.shared.domain.model.Message;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PricePersistencePort {

  public static final String GET_PRICE_BY_PROPERTIES_ADDRESS = "getPriceByPropertiesAddress";

  public PricePersistencePort(){
  }

  public List<Price> getAllPricesByProperties(PriceFilter filter) {
    return requestEvent(new Message<>(GET_PRICE_BY_PROPERTIES_ADDRESS, filter));
  }

}