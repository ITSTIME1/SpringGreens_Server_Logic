package com.spring_greens.logic.extraction.converter.ifs;

import com.spring_greens.logic.global.dto.MallDTO;
import com.spring_greens.logic.global.dto.ProductDTO;
import com.spring_greens.logic.global.dto.ShopDTO;
import com.spring_greens.logic.global.entity.Mall;
import com.spring_greens.logic.global.entity.Product;
import com.spring_greens.logic.global.entity.Shop;

public interface ExtractionConverter {
    MallDTO craeteMallDTO(Mall mall);
    ProductDTO createProductDTO(Product product);
    ShopDTO createShopDTO(Shop shop);
}
