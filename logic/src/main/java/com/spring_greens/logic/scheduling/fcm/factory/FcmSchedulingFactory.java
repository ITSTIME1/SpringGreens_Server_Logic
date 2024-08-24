package com.spring_greens.logic.scheduling.fcm.factory;
import com.spring_greens.logic.scheduling.fcm.entity.FcmReservationMessage;
import com.spring_greens.logic.scheduling.global.enums.MallType;
import com.spring_greens.logic.scheduling.fcm.factory.ifs.FactoryOperation;
import com.spring_greens.logic.scheduling.fcm.repository.FcmSchedulingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class FcmSchedulingFactory implements FactoryOperation {


    private final FcmSchedulingRepository fcmSchedulingRepository;

    public FcmSchedulingFactory(FcmSchedulingRepository fcmSchedulingRepository) {
        this.fcmSchedulingRepository = fcmSchedulingRepository;
    }

    @Override
    public List<FcmReservationMessage> getReservationMessages(String mallType, String currentTime) {
        if(mallType.equals(MallType.APM.getName())) {
            return fcmSchedulingRepository.findReservationMessageByReserveDateTimeFromApm(currentTime);
        } else if(mallType.equals(MallType.CHUNGPEONG.getName())) {
            return fcmSchedulingRepository.findReservationMessageByReserveDateTimeFromChung(currentTime);
        } else if(mallType.equals(MallType.JEIL.getName())) {
            return fcmSchedulingRepository.findReservationMessageByReserveDateTimeFromJeil(currentTime);
        } else if(mallType.equals(MallType.DONGPEONG.getName())) {
            return fcmSchedulingRepository.findReservationMessageByReserveDateTimeFromDong(currentTime);
        }
        return null;
    }
}

