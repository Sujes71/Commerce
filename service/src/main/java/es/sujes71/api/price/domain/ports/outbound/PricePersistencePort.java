package es.sujes71.api.price.domain.ports.outbound;

import static es.sujes71.shared.domain.ports.outbound.OutboundPort.requestEvent;

import es.sujes71.api.price.domain.model.PriceFilter;
import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import es.sujes71.shared.domain.model.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PricePersistencePort {

	public static final String GET_PRICE_BY_PROPERTIES_ADDRESS = "getPriceByPropertiesAddress";

	public PricePersistencePort(){
	}

	public Mono<PriceEntity> getPriceByProperties(PriceFilter filter) {
		return requestEvent(new Message<>(GET_PRICE_BY_PROPERTIES_ADDRESS, filter));
	}

}
