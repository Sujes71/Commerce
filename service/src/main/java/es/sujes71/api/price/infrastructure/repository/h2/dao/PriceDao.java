package es.sujes71.api.price.infrastructure.repository.h2.dao;

import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PriceDao extends ReactiveCrudRepository<PriceEntity, Long> {

  @Query("""
        SELECT * FROM PRICES
        WHERE BRAND_ID = :brandId
        AND PRODUCT_ID = :productId
        AND :applicationDate BETWEEN START_DATE AND END_DATE
        ORDER BY PRIORITY DESC
        LIMIT 1
        """)
  Mono<PriceEntity> findByProperties(Integer brandId, Integer productId, LocalDateTime applicationDate);
}