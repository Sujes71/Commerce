package es.sujes71.api.price.rest;

import static es.sujes71.api.price.rest.adapter.PriceFilterAdapter.adapt;
import static es.sujes71.shared.rest.Routing.BASE_PATH;
import static es.sujes71.shared.rest.Routing.GET_PRICE_BY_PROPERTIES_PATH;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(BASE_PATH)
public class PriceController {

  private final GetPriceByPropertiesUseCase getPriceByPropertiesUseCase;

  public PriceController(GetPriceByPropertiesUseCase getPriceByPropertiesUseCase) {
    this.getPriceByPropertiesUseCase = getPriceByPropertiesUseCase;
  }

  @GetMapping(GET_PRICE_BY_PROPERTIES_PATH)
  public Mono<Price> getPrice(
      @RequestParam Integer brandId,
      @RequestParam Integer productId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
    return getPriceByPropertiesUseCase.execute(adapt(brandId, productId, applicationDate));
  }

}
