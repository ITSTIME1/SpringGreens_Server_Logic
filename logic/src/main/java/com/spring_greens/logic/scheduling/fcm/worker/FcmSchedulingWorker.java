package com.spring_greens.logic.scheduling.fcm.worker;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.spring_greens.logic.scheduling.global.ifs.SchedulingOperation;
import com.spring_greens.logic.scheduling.fcm.dto.TopicDetails;
import com.spring_greens.logic.scheduling.fcm.entity.FcmReservationMessage;
import com.spring_greens.logic.scheduling.global.enums.MallType;
import com.spring_greens.logic.scheduling.fcm.factory.FcmSchedulingFactory;

import com.spring_greens.logic.scheduling.fcm.repository.FcmSubscriptionRepository;
import com.spring_greens.logic.scheduling.fcm.repository.FcmTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This is FcmScheduling Service Worker.<br>
 * this is responsible for sending cloud messages. to authenticated users. <br>
 * FcmSchedulingWorker should process related to 'scheduling'. <br>
 *
 * @author itstime0809
 */
@Slf4j
@Service
public class FcmSchedulingWorker implements SchedulingOperation {

    private final FcmSubscriptionRepository fcmSubscriptionRepository;
    private final FcmTokenRepository fcmTokenRepository;

    private final FcmSchedulingFactory fcmSchedulingFactory;

    // topicName : [userFcmToken]
    private Map<String, List<String>> topicNamesHasUserTokenTb = new ConcurrentHashMap<>();
    private Map<String, TopicDetails> topicNamesHasTopicDetailsTb = new ConcurrentHashMap<>();
    private Set<String> topicNamesSet = new HashSet<>();

    private final RedisTemplate<String, String> redisTemplate;




    public FcmSchedulingWorker(FcmSubscriptionRepository fcmSubscriptionRepository, FcmTokenRepository fcmTokenRepository, FcmSchedulingFactory fcmSchedulingFactory, RedisTemplate<String, String> redisTemplate) {
        this.fcmSubscriptionRepository = fcmSubscriptionRepository;
        this.fcmTokenRepository = fcmTokenRepository;
        this.fcmSchedulingFactory = fcmSchedulingFactory;
        this.redisTemplate = redisTemplate;
    }



    /**
     * doScheduling work every minutes <br>
     * @Scheduled annotation has a cron expressions for scheduling. <br>
     *
     * @author itstime0809
     */
    @Scheduled(cron = "0 * * * * *")
    public void doScheduling() {
        Arrays.stream(MallType.values()).parallel().forEach(mallType -> {
            log.info("Scheduled task started for mall: {}", mallType.getName());
            processReservationMessages(mallType.getName());
        });
        topicNamesHasUserTokenTb.clear();
        topicNamesHasTopicDetailsTb.clear();
    }


    /**
     * Retrieve reservation messages at a specific time from 'fcm_reservation_message_***' <br>
     * MallType parameter determines where to get the data. <br>
     *
     *
     * @param mallType
     */
    private void processReservationMessages(String mallType) {
        log.info("Execute retrieveReservationMessages for mall: {}", mallType);

        try {
            List<FcmReservationMessage> retrieveMessagesList = getRetrieveMessages(mallType);

            if(retrieveMessagesList == null || retrieveMessagesList.isEmpty()) {
                log.error("Couldn't find reservation messages");
                return;
            }

            log.info("Success get retrieveMessageList : {}", mallType);

            setTopicDetails(retrieveMessagesList);

            log.info("Success set topicDetails : {}", mallType);

            getUserIdFromRedis(mallType);

            log.info("Success get userId from redis : {}", mallType);


            sendNotificationToChannel();

            log.info("Success send Notification : {}", mallType);
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }


    /**
     * Set TopicDetails that will be used sendNotificationToChannel method.
     * @param fcmReservationMessageList
     * @author itstime0809
     */
    private void setTopicDetails (List<FcmReservationMessage> fcmReservationMessageList) {

        for (FcmReservationMessage fcmReservationMessage : fcmReservationMessageList) {
            String topicName = fcmReservationMessage.getTopicName();
            String title = fcmReservationMessage.getTitle();
            String body = fcmReservationMessage.getBody();
            String imagePath = fcmReservationMessage.getImagePath();
            topicNamesSet.add(topicName);

            topicNamesHasUserTokenTb.putIfAbsent(topicName, new ArrayList<>());

            TopicDetails topicDetails = TopicDetails.builder()
                    .title(title)
                    .body(body)
                    .imagePath(imagePath)
                    .build();

            topicNamesHasTopicDetailsTb.putIfAbsent(topicName, topicDetails);
        }
    }

    /**
     * Get userId from redis. when user connect to mainPage. <br>
     * first. verify user jwt token by Spring security.
     * second. can connect to webSocket server and find userId on accessToken for saving redis.
     * third. saved userId will be used here, and a message will be sent if the user is connected to a specific mall, moves to main page, and subscribes to a specific store.
     * @author itstime0809
     */
    private void getUserIdFromRedis(String mallType) {

        /**
         * 실제 redis에서 userId를 추출하는 로직, 레디스에다가 구현.
         */
        // 4. 레디스에서 현재 접속해 있는 유저들의 id를 가지고 온다.
        log.info(mallType+"Channel");
        List<String> userIds = redisTemplate.opsForList().range(mallType+"Channel", 0, -1);

        assert userIds != null;

        log.info("{} redis user ids size {}", mallType, userIds.size());

        // 5. 해당 유저id들이 현재 발송할 topicNames를 구독하고 있는지 확인한다.
        for (String userId : userIds) {

            List<String> subscriptionList = fcmSubscriptionRepository.findAllTopicNamesByUserId(Integer.valueOf(userId));

            if (subscriptionList.isEmpty()) {
                continue;
            }

            String userFcmToken = fcmTokenRepository.findFcmTokenByUserId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("Couldn't find FCM token for user ID: " + userId));


            Set<String> subscriptionSet = new HashSet<>(subscriptionList);

            subscriptionSet.retainAll(topicNamesSet);

            for (String topicName : subscriptionSet) {
                topicNamesHasUserTokenTb.computeIfAbsent(topicName,
                        k -> new ArrayList<>()).add(userFcmToken);
            }
        }
    }

    /**
     * Get retrieve messages.
     * @param mallType
     */
    private List<FcmReservationMessage> getRetrieveMessages(String mallType) throws NullPointerException{
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return fcmSchedulingFactory.getReservationMessages(mallType, currentDateTime);
    }

    /**
     * Send fcmMessage to users. but make sure fcm can only send it to 500 users at a time.
     * @author itstime0809
     */
    private void sendNotificationToChannel() {

        for (Map.Entry<String, List<String>> entry : topicNamesHasUserTokenTb.entrySet()) {
            String topicName = entry.getKey();
            List<String> userFcmTokenList = entry.getValue();

            if(userFcmTokenList == null || userFcmTokenList.isEmpty()) {
                log.info("{} hasn't userFcmTokenList", topicName);
                continue;
            }

            // can send to 500 users at a time.
            for (String token : userFcmTokenList) {
                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(
                                Notification.builder()
                                        .setTitle(topicNamesHasTopicDetailsTb.get(topicName).getTitle())
                                        .setBody(topicNamesHasTopicDetailsTb.get(topicName).getBody())
                                        .setImage(topicNamesHasTopicDetailsTb.get(topicName).getImagePath())
                                        .build()).build();

                log.info("Sending message to token: {}", token);
                try {
                    String response = FirebaseMessaging.getInstance().send(message);
                    log.info("Successfully sent FCM message to token: {} with response: {}", token, response);
                } catch (FirebaseMessagingException e) {
                    log.error("Failed to send FCM message to token: {}: {}", token, e.getMessage());
                } catch (Exception e) {
                    log.error("Unexpected error while sending FCM message to token: {}: {}", token, e.getMessage());
                }
            }
        }
    }
}
