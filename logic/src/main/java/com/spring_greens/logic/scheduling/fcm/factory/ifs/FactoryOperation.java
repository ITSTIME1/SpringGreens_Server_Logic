package com.spring_greens.logic.scheduling.fcm.factory.ifs;

import com.spring_greens.logic.scheduling.fcm.entity.FcmReservationMessage;

import java.util.List;


/**
 * Determine FactoryOperation
 */
public interface FactoryOperation {
    List<FcmReservationMessage> getReservationMessages(String mallType, String currentDateTime);
}
