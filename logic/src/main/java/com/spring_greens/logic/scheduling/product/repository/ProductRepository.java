package com.spring_greens.logic.scheduling.product.repository;
import com.spring_greens.logic.scheduling.product.dto.projection.ExtractedProducts;
import com.spring_greens.logic.scheduling.product.entity.Product;
import com.spring_greens.logic.scheduling.redis.dto.ScheduledRedisProduct;
import com.spring_greens.logic.scheduling.redis.dto.projection.RedisProductProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query(value = "select id, daily_ad_impressions, details_product_click_count, total_viewers from product_apm where stock_status=true and extractable_status=true and TIMESTAMPDIFF(MINUTE, last_extract_date, NOW()) != 5 and age!=0", nativeQuery = true)
    List<ExtractedProducts> extractProductsFromApm();

    @Query(value = "select id, daily_ad_impressions, details_product_click_count, total_viewers from product_dong where stock_status=true and extractable_status=true and TIMESTAMPDIFF(MINUTE, last_extract_date, NOW()) != 5 and age!=0", nativeQuery = true)
    List<ExtractedProducts> extractProductsFromDong();

    @Query(value = "select id, daily_ad_impressions, details_product_click_count, total_viewers from product_chung where stock_status=true and extractable_status=true and TIMESTAMPDIFF(MINUTE, last_extract_date, NOW()) != 5 and age!=0", nativeQuery = true)
    List<ExtractedProducts> extractProductsFromChung();

    @Query(value = "select id, daily_ad_impressions, details_product_click_count, total_viewers from product_jeil where stock_status=true and extractable_status=true and TIMESTAMPDIFF(MINUTE, last_extract_date, NOW()) != 5 and age!=0", nativeQuery = true)
    List<ExtractedProducts> extractProductsFromJeil();



    @Query(value = "SELECT s.id AS shopId, s.name AS shopName, s.contact AS shopContact, s.address_details AS shopAddressDetails, " +
            "p.id AS productId, p.name AS productName, p.unit AS productUnit, p.price AS productPrice " +
            "FROM shop s " +
            "JOIN product_apm p ON s.id = p.shop_id " +
            "WHERE p.id IN :productIds", nativeQuery = true)
    List<RedisProductProjection> findProductInformationFromApm(List<Long> productIds);

    @Query(value = "SELECT s.id AS shopId, s.name AS shopName, s.contact AS shopContact, s.address_details AS shopAddressDetails, " +
            "p.id AS productId, p.name AS productName, p.unit AS productUnit, p.price AS productPrice " +
            "FROM shop s " +
            "JOIN product_dong p ON s.id = p.shop_id " +
            "WHERE p.id IN :productIds", nativeQuery = true)
    List<RedisProductProjection> findProductInformationFromDong(List<Long> productIds);

    @Query(value = "SELECT s.id AS shopId, s.name AS shopName, s.contact AS shopContact, s.address_details AS shopAddressDetails, " +
            "p.id AS productId, p.name AS productName, p.unit AS productUnit, p.price AS productPrice " +
            "FROM shop s " +
            "JOIN product_chung p ON s.id = p.shop_id " +
            "WHERE p.id IN :productIds", nativeQuery = true)
    List<RedisProductProjection> findProductInformationFromChung(List<Long> productIds);

    @Query(value = "SELECT s.id AS shopId, s.name AS shopName, s.contact AS shopContact, s.address_details AS shopAddressDetails, " +
            "p.id AS productId, p.name AS productName, p.unit AS productUnit, p.price AS productPrice " +
            "FROM shop s " +
            "JOIN product_jeil p ON s.id = p.shop_id " +
            "WHERE p.id IN :productIds", nativeQuery = true)
    List<RedisProductProjection> findProductInformationFromJeil(List<Long> productIds);
}
