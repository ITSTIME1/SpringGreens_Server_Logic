package com.spring_greens.logic.global.repository;

import java.util.List;
import java.util.Optional;

import com.spring_greens.logic.global.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring_greens.logic.global.dto.ShopDTO;
import com.spring_greens.logic.global.entity.Shop;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    @Query(value = "SELECT s.id, s.mall_id, s.name, " +
            "s.contact, s.address_details " +
            "FROM shop as s INNER JOIN product as p ON p.id = s.product_id " +
            "WHERE p.id IN :productIds")
    Optional<List<ShopDTO>> findByProduct(@Param("productIds") List<Long> ProductId);
}
