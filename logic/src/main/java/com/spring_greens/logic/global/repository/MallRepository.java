package com.spring_greens.logic.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring_greens.logic.global.dto.MallDTO;
import com.spring_greens.logic.global.entity.Mall;

@Repository
public interface MallRepository extends CrudRepository<Mall, Long>{    
    @Query()
    Optional<List<MallDTO>> findByShop();
}
