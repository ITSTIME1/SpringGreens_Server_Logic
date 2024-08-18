package com.spring_greens.logic.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring_greens.logic.global.dto.ProductDTO;
import com.spring_greens.logic.global.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{
    @Query(value = "select p.id, " +
    "p.shop_id, " +
    "p.name, " +
    "p.unit, " +
    "p.price, " +
    "p.total_viewers, " +
    "p.age, " +
    "p.detail_product_click_count, " +
    "p.daily_ad_impressions, "+
    "p.extractable_status, " +
    "p.last_extract_date " +
    "from product as p where p.stock_status = 1 and p.extractable_status = 1", nativeQuery = true)
    Optional<List<ProductDTO>> findAllAge();
}
