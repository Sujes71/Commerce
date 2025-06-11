package es.sujes71.api.price.infrastructure.repository.h2.entity;

import es.sujes71.api.price.domain.model.Price;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("PRICES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntity {

  @Id
  private Long id;

  @Column("BRAND_ID")
  @NotNull
  private Integer brandId;

  @Column("START_DATE")
  @NotNull
  private LocalDateTime startDate;

  @Column("END_DATE")
  @NotNull
  private LocalDateTime endDate;

  @Column("PRICE_LIST")
  @NotNull
  private Integer priceList;

  @Column("PRODUCT_ID")
  @NotNull
  private Integer productId;

  @Column("PRIORITY")
  @NotNull
  private Integer priority;

  @Column("PRICE")
  @NotNull
  @Positive
  private BigDecimal price;

  @Column("CURR")
  @NotNull
  private String currency;

  public Price toDomain() {
    return Price.builder()
        .brandId(this.brandId)
        .startDate(this.startDate)
        .endDate(this.endDate)
        .priceList(this.priceList)
        .productId(this.productId)
        .priority(this.priority)
        .price(this.price)
        .currency(this.currency)
        .build();
  }
}