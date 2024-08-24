package com.spring_greens.logic.scheduling.fcm.repository;

import com.spring_greens.logic.scheduling.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends CrudRepository<FcmToken, Long> {
    @Query("SELECT f.token FROM FcmToken f WHERE f.userId = :userId")
    Optional<String> findFcmTokenByUserId(@Param("userId") Long userId);
}
