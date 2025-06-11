package es.sujes71.api.price.rest;

import static es.sujes71.api.price.rest.adapter.PriceFilterAdapter.adapt;
import static es.sujes71.api.price.rest.validator.PriceParameterValidator.validate;
import static es.sujes71.shared.rest.Routing.BASE_PATH;
import static es.sujes71.shared.rest.Routing.GET_PRICE_BY_PROPERTIES_PATH;

import es.sujes71.api.price.domain.model.Price;
import es.sujes71.api.price.domain.ports.inbound.GetPriceByPropertiesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE_PATH)
public class PriceController {

  private final GetPriceByPropertiesUseCase getPriceByPropertiesUseCase;

  public PriceController(GetPriceByPropertiesUseCase getPriceByPropertiesUseCase) {
    this.getPriceByPropertiesUseCase = getPriceByPropertiesUseCase;
  }

  @GetMapping(GET_PRICE_BY_PROPERTIES_PATH)
  public ResponseEntity<Price> getPrice(
      @RequestParam(required = false) String brandId,
      @RequestParam(required = false) String productId,
      @RequestParam(required = false) String applicationDate) {

    validate(brandId, productId, applicationDate);
    Price price = getPriceByPropertiesUseCase.execute(adapt(brandId, productId, applicationDate));
    return ResponseEntity.ok(price);
  }
}