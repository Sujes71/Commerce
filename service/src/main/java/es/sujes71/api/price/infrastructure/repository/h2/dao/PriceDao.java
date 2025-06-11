package es.sujes71.api.price.infrastructure.repository.h2.dao;

import es.sujes71.api.price.infrastructure.repository.h2.entity.PriceEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PriceDao extends CrudRepository<PriceEntity, Long> {

  @Query("""
    SELECT * FROM PRICES
    WHERE BRAND_ID = :brandId
    AND PRODUCT_ID = :productId
    AND :applicationDate BETWEEN START_DATE AND END_DATE
    """)
  List<PriceEntity> findAllByProperties(Integer brandId, Integer productId, LocalDateTime applicationDate);
}