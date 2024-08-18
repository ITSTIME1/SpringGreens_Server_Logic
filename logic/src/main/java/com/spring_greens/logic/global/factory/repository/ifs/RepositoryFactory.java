package com.spring_greens.logic.global.factory.repository.ifs;

import com.spring_greens.logic.global.repository.MallRepository;
import com.spring_greens.logic.global.repository.ProductRepository;
import com.spring_greens.logic.global.repository.ShopRepository;

public interface RepositoryFactory {
    MallRepository getMallRepository();

    ProductRepository getProductRepository();

    ShopRepository getShopRepository();
}
