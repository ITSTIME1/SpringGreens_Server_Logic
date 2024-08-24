package com.spring_greens.logic.scheduling.redis.worker;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_greens.logic.scheduling.global.enums.MallType;
import com.spring_greens.logic.scheduling.global.ifs.SchedulingOperation;
import com.spring_greens.logic.scheduling.product.dto.projection.ExtractedProducts;
import com.spring_greens.logic.scheduling.product.repository.ProductRepository;
import com.spring_greens.logic.scheduling.redis.dto.ScheduledRedisProduct;
import com.spring_greens.logic.scheduling.redis.dto.projection.RedisProductProjection;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExtractRedisProductWorker implements SchedulingOperation {
    @Getter
    @Builder
    static class Probabilities {
        private long id;
        private double probabilities;
    }

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private static final double[] WEIGHTS = new double[]{0.2,0.3,0.2,0.3};

    // 추출할 상품의 개수
    private static final byte MAX_SELECTED_ITEM_LIST = 10;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        // webLcient 초기화
        webClient = WebClient.builder().baseUrl("http://localhost:8080/api/product").build();
    }


    /**
     * 스케줄링을 돌리게 되는데
     * 이때 상품을 추출할때 데이터까지 다 가지고 와야 될거 같은데
     */
    @Scheduled(cron = "0 0/10 * * * *")
    @Override
    public void doScheduling() {
        Arrays.stream(MallType.values()).parallel().forEach(mallType -> {

            List<ExtractedProducts> extractedProductsList = extractProductsByUsing(mallType.getName());
            if(extractedProductsList == null || extractedProductsList.isEmpty()) {
                log.error("Couldn't find extractProducts : {}", mallType);
                return;
            }
            log.info("Success extract products {}", mallType);
            log.info("ExtractedProduct size {}, size {}", mallType, extractedProductsList.size());

            // 만약 추출된 상품의 개수가 20개 미만이라면 진행하지 않는다.
            if(extractedProductsList.size() < 10) {
                return;
            }

            Map<Long, Double> extractedProductAgeScoreTb = new ConcurrentHashMap<>();
            Map<Long, Integer> extractedProductNewAgeTb = new ConcurrentHashMap<>();



            double totalAgeScore = 0.0;

            for (ExtractedProducts extractedProducts : extractedProductsList) {
                double ageScore = extractedProducts.getDailyAdImpressions() * WEIGHTS[0]
                        + extractedProducts.getDetailsProductClickCount() * WEIGHTS[1]
                        + extractedProducts.getTotalViewers() * WEIGHTS[2];

                double weightedAgeScore = ageScore * WEIGHTS[3];
                totalAgeScore += weightedAgeScore;
                extractedProductAgeScoreTb.put(extractedProducts.getId(), ageScore);
            }
            double copyTotalAgeScore = totalAgeScore;

            extractedProductsList.forEach(extractedProducts -> {
                long extractedProductId = extractedProducts.getId();
                int age = getAge(extractedProductId, extractedProductAgeScoreTb, copyTotalAgeScore);
                extractedProductNewAgeTb.put(extractedProductId, age);
            });

            log.info("Success set new age and save ageScore : {}", mallType);

            // 이제 룰렛 휠 알고리즘을 돌려야겠네
            List<Long> extractedProductsByAlgorithm = rouletteAlgorithm(mallType.getName(), extractedProductAgeScoreTb, totalAgeScore);

            // 그럼 이제 10개의 상품이 뽑혔을테니, 그 10개의 상품들의 id값을 가지고 이제 보낼 정보를 가지고 오자.

            log.info("extractedProductsByAlgorithm {}, result id size : {}", mallType, extractedProductsByAlgorithm.size());
            // 그럼 여기에 10개의 상품들을 만들어 왔을거고,
            List<RedisProductProjection> redisProductProjectionsList = null;
            if(mallType.getName().equals(MallType.APM.getName())) {
                redisProductProjectionsList = productRepository.findProductInformationFromApm(extractedProductsByAlgorithm);
            } else if (mallType.getName().equals(MallType.CHUNGPEONG.getName())) {
                redisProductProjectionsList = productRepository.findProductInformationFromChung(extractedProductsByAlgorithm);
            } else if (mallType.getName().equals(MallType.JEIL.getName())) {
                redisProductProjectionsList = productRepository.findProductInformationFromJeil(extractedProductsByAlgorithm);
            } else if (mallType.getName().equals(MallType.DONGPEONG.getName())) {
                redisProductProjectionsList = productRepository.findProductInformationFromDong(extractedProductsByAlgorithm);
            }

            // 현재 여기가 문제인데
            if(redisProductProjectionsList == null || redisProductProjectionsList.isEmpty()) {
                log.error("{} fail extracting redisProduct", mallType);
                log.error("{} exit scheduling", mallType);
                return;
            }


            log.info("redisProductProjectionsList size : {}", redisProductProjectionsList.size());
//            for (RedisProductProjection r : redisProductProjectionsList) {
//                log.info("Product Information : {}", mallType);
//                log.info(String.valueOf(r.getProductId()), r.getProductName(), r.getProductPrice(), r.getProductUnit());
//
//                log.info("Shop information : {}", mallType);
//                log.info(r.getShopName(), r.getShopAddressDetails(), r.getShopId(), r.getShopContact());
//            }

            log.info("{} success extracting redisProduct information", mallType);

            ScheduledRedisProduct scheduledRedisProduct = null;

            if(mallType.getName().equals(MallType.APM.getName())) {
                scheduledRedisProduct = ScheduledRedisProduct.builder()
                        .mall_id((long) MallType.APM.getId())
                        .mall_name(MallType.APM.getName())
                        .build();
            } else if(mallType.getName().equals(MallType.DONGPEONG.getName())) {
                scheduledRedisProduct = ScheduledRedisProduct.builder()
                        .mall_id((long) MallType.DONGPEONG.getId())
                        .mall_name(MallType.DONGPEONG.getName())
                        .build();
            } else if(mallType.getName().equals(MallType.CHUNGPEONG.getName())) {
                scheduledRedisProduct = ScheduledRedisProduct.builder()
                        .mall_id((long) MallType.CHUNGPEONG.getId())
                        .mall_name(MallType.CHUNGPEONG.getName())
                        .build();
            } else if(mallType.getName().equals(MallType.JEIL.getName())) {
                scheduledRedisProduct = ScheduledRedisProduct.builder()
                        .mall_id((long) MallType.JEIL.getId())
                        .mall_name(MallType.JEIL.getName())
                        .build();
            }

            log.info("Success created SchduledRedisProduct : {}", mallType);


            // redisProductProjection에 있는것을 전부 가지고 온 다음에 ( 10 개 상품에 대한 것 )
            List<ScheduledRedisProduct.Shop> shopList = new ArrayList<>(10);
            for (RedisProductProjection redisProductProjection : redisProductProjectionsList) {

                ScheduledRedisProduct.Shop.RedisProduct product = ScheduledRedisProduct.Shop.RedisProduct.builder()
                        .product_id(redisProductProjection.getProductId())
                        .product_name(redisProductProjection.getProductName())
                        .product_unit(redisProductProjection.getProductUnit())
                        .product_price(redisProductProjection.getProductPrice())
                        .product_image_url("/images/test/example1.jpg")
                        .build();

                log.info("Builder ScheduledProduct : {}", mallType);
                log.info(product.getProduct_name(), product.getProduct_price());

                ScheduledRedisProduct.Shop shop = ScheduledRedisProduct.Shop.builder()
                        .shop_id(redisProductProjection.getShopId())
                        .shop_name(redisProductProjection.getShopName())
                        .shop_contact(redisProductProjection.getShopContact())
                        .shop_address_details(redisProductProjection.getShopAddressDetails())
                        .product(List.of(product))
                        .build();
                log.info("Builder ScheduledShop : {}", mallType);
                log.info(shop.getShop_name());
                shopList.add(shop);
            }


            // 추출후에, 데이터베이스에 반영해야 함

            // send to presentation api
            ScheduledRedisProduct result = scheduledRedisProduct.toBuilder().shop_list(shopList).build();
            try {
                sendToExternalApi(result);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        });
    }
    private List<Long> rouletteAlgorithm(String mallType, Map<Long, Double> extractedProductAgeScoreTb, double totalAgeScore) {

        log.info("Start RouletteAlgorithm : {}", mallType);
        // 확률을 담아둘 변수
        List<Probabilities> productProbabilities = new ArrayList<>();

        // 누적 확률 값을 저장
        List<Double> cumulativeProbabilities = new ArrayList<>();

        // 10개의 상품을 선택할 리스트를 선정, productId만 받아준다.
        List<Long> selectedItemList = new ArrayList<>(MAX_SELECTED_ITEM_LIST);

        double totalProbabilities = 0.0;
        // 각 상품에 대한 확률을 계산해서 리스트에 적재
        for (Map.Entry<Long, Double> product : extractedProductAgeScoreTb.entrySet()) {
            long productId = product.getKey();
            double ageScore = product.getValue();
            double probabilities = (ageScore * WEIGHTS[3]) / totalAgeScore;
            totalProbabilities += probabilities;

            Probabilities probabilitiesProduct = Probabilities.builder()
                    .id(productId)
                    .probabilities(probabilities)
                    .build();

            productProbabilities.add(probabilitiesProduct);
            cumulativeProbabilities.add(totalProbabilities);
        }


        // 20개의 cumulative가 정상적으로 나오게 되었고
        log.info("CumulativeProbabilities {} : {}", mallType, cumulativeProbabilities.size());

        log.info("Success setting probabilities {}", mallType);

        Random random = new Random();
        while (selectedItemList.size() < MAX_SELECTED_ITEM_LIST) {
            double r = random.nextDouble();

            for (int i = 0; i < cumulativeProbabilities.size(); i++) {
                if (r <= cumulativeProbabilities.get(i)) {

                    if(selectedItemList.contains(productProbabilities.get(i).getId())) {
                        continue;
                    }
                    selectedItemList.add(productProbabilities.get(i).getId());
                    break;
                }
            }
        }

        log.info("Successful selected 10 items");

        return selectedItemList;
    }
    private int getAge(long extractedProductId, Map<Long, Double> extractedProductAgeScoreTb, double totalAgeScore){
        // 지금까지 구한 ageScore로 구하면 되겠다. i에 대한 ageScore / totalAgescore
        double ageScore = extractedProductAgeScoreTb.get(extractedProductId);

        // 0~1 사이의 값으로 정규화를 진행.
        double normalizedScore = ageScore / totalAgeScore;

        // 정규화된 값을 1~5사이로 조정
        int age = (int)Math.round(normalizedScore * 4) + 1;

        // 조정된 값이 만약 5를 넘어가게 된다면 upperbound로 설정. 혹은 lowerbound인 1보다 작아진다면 1로 설정
        return Math.max(1, Math.min(age, 5));
    }

    /**
     * Extract products
     * @author itstime0809
     */
    private List<ExtractedProducts> extractProductsByUsing(String mallType) {
        if(mallType.equals(MallType.APM.getName())) {
            return productRepository.extractProductsFromApm();
        } else if (mallType.equals(MallType.CHUNGPEONG.getName())) {
            return productRepository.extractProductsFromChung();
        } else if (mallType.equals(MallType.JEIL.getName())) {
            return productRepository.extractProductsFromJeil();
        } else if (mallType.equals(MallType.DONGPEONG.getName())) {
            return productRepository.extractProductsFromDong();
        }
        return null;
    }



    // 다른 기존 코드

    private void sendToExternalApi(ScheduledRedisProduct scheduledRedisProduct) throws JsonProcessingException {

        String jsonString = objectMapper.writeValueAsString(scheduledRedisProduct);
        log.info(jsonString);
        webClient.post()
                .uri("/get/scheduled_redis_product")
                .body(Mono.just(scheduledRedisProduct), ScheduledRedisProduct.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("Successfully sent data to external API"))
                .doOnError(error -> log.error("Failed to send data to external API", error))
                .subscribe(); // 비동기적으로 처리
    }

}
