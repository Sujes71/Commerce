package es.sujes71.api.price.domain.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {
  private Integer brandId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer productId;
  private Integer priority;
  private BigDecimal price;
  private String currency;
  private Integer priceList;

}
