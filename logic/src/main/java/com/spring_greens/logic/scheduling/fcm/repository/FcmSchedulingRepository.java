package com.spring_greens.logic.scheduling.fcm.repository;

import com.spring_greens.logic.scheduling.fcm.entity.FcmReservationMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FcmSchedulingRepository extends CrudRepository<FcmReservationMessage, Long> {

    @Query(value = "select * from fcm_reservation_message_apm where reserve_date_time = :reserveDateTime", nativeQuery = true)
    List<FcmReservationMessage> findReservationMessageByReserveDateTimeFromApm(@Param("reserveDateTime") String reserveDateTime);

    @Query(value = "select * from fcm_reservation_message_chung where reserve_date_time = :reserveDateTime", nativeQuery = true)
    List<FcmReservationMessage> findReservationMessageByReserveDateTimeFromChung(@Param("reserveDateTime") String reserveDateTime);

    @Query(value = "select * from fcm_reservation_message_dong where reserve_date_time = :reserveDateTime", nativeQuery = true)
    List<FcmReservationMessage> findReservationMessageByReserveDateTimeFromDong(@Param("reserveDateTime") String reserveDateTime);

    @Query(value = "select * from fcm_reservation_message_jeil where reserve_date_time = :reserveDateTime", nativeQuery = true)
    List<FcmReservationMessage> findReservationMessageByReserveDateTimeFromJeil(@Param("reserveDateTime") String reserveDateTime);

}
