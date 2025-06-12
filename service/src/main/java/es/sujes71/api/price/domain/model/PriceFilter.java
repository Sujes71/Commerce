package es.sujes71.api.price.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceFilter {
  private Integer brandId;
  private Integer productId;
  private LocalDateTime applicationDate;

}
