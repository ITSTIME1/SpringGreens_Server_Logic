package com.spring_greens.logic.scheduling.fcm.repository;


import com.spring_greens.logic.scheduling.fcm.entity.FcmSubscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FcmSubscriptionRepository extends CrudRepository<FcmSubscription, Long> {
    // 특정 유저가 구독한 모든 topicName을 가져오는 메서드
    @Query("SELECT fs.topicName FROM FcmSubscription fs WHERE fs.userId = :userId")
    List<String> findAllTopicNamesByUserId(@Param("userId") Integer userId);
}
