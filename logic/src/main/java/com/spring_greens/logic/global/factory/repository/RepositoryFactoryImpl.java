package com.spring_greens.logic.global.factory.repository;

import org.springframework.stereotype.Component;

import com.spring_greens.logic.global.factory.repository.ifs.RepositoryFactory;
import com.spring_greens.logic.global.repository.MallRepository;
import com.spring_greens.logic.global.repository.ProductRepository;
import com.spring_greens.logic.global.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RepositoryFactoryImpl implements RepositoryFactory{
    
    private final MallRepository mallrepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Override
    public MallRepository getMallRepository() {
        return mallrepository;
    }

    @Override
    public ProductRepository getProductRepository() {
        return productRepository;
    }

    @Override
    public ShopRepository getShopRepository() {
        return shopRepository;
    }
    
}
