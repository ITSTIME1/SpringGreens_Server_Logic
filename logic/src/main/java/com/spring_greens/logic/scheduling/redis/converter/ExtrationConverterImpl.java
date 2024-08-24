//package com.spring_greens.logic.scheduling.redis.converter;
//
//import com.spring_greens.logic.scheduling.redis.converter.ifs.ExtractionConverter;
//import com.spring_greens.logic.global.dto.MallDTO;
//import com.spring_greens.logic.global.dto.ProductDTO;
//import com.spring_greens.logic.global.dto.ShopDTO;
//import com.spring_greens.logic.global.entity.Mall;
//import com.spring_greens.logic.global.entity.Product;
//import com.spring_greens.logic.global.entity.Shop;
//
//public class ExtrationConverterImpl implements ExtractionConverter {
//
//    @Override
//    public MallDTO craeteMallDTO(Mall mall) {
//        return MallDTO.builder()
//                    .id(mall.getId())
//                    .name(mall.getName())
//                    .build();
//    }
//
//    @Override
//    public ProductDTO createProductDTO(Product product) {
//        return ProductDTO.builder()
//                    .id(product.getId())
//                    .name(product.getName())
//                    .unit(product.getUnit())
//                    .price(product.getPrice())
//                    .totalViewers(product.getTotalViewers())
//                    // .productImageUrl(product.get)
//                    // .majorCategory(product.getMajorCategory())
//                    // .subCategory(product.getSubCategory())
//                    .detailsProductClickCount(product.getDetailsProductClickCount())
//                    .dailyAdImpressions(product.getDailyAdImpressions())
//                    .age(product.getAge())
//                    .build();
//    }
//
//    @Override
//    public ShopDTO createShopDTO(Shop shop) {
//        return ShopDTO.builder()
//                    .id(shop.getId())
//                    .name(shop.getName())
//                    .contact(shop.getContact())
//                    .addressDetails(shop.getAddressDetails())
//                    .build();
//    }
//
//
//}
