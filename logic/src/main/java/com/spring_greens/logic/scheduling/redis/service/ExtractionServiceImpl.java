//package com.spring_greens.logic.scheduling.redis.service;
//
//import java.util.*;
//
//import com.spring_greens.logic.scheduling.redis.dto.RedisProduct;
//import com.spring_greens.logic.scheduling.redis.dto.RedisShop;
//import com.spring_greens.logic.global.dto.ShopDTO;
//import com.spring_greens.logic.global.enums.Mall;
//import com.spring_greens.logic.scheduling.redis.service.ifs.ExtractionService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.spring_greens.logic.scheduling.redis.dto.RedisDTO;
//import com.spring_greens.logic.global.dto.ProductDTO;
//import com.spring_greens.logic.global.factory.converter.ifs.ConverterFactory;
//import com.spring_greens.logic.global.factory.repository.ifs.RepositoryFactory;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ExtractionServiceImpl implements ExtractionService {
//
//    private final static Logger logger = LoggerFactory.getLogger(ExtractionServiceImpl.class);
//
//    private final ConverterFactory converterFactory;
//    private final RepositoryFactory repositoryFactory;
//    private final Double[] weights = {0.2, 0.3, 0.5};
//    private double sum = 0.0;
//    private double max = 0.0;
//
//
//    public RedisDTO ExtractionProductService(){
//
//        // 상품을추출
//        List<ProductDTO> products = createProducts();
//
//        // 확률
//        List<Double> Probabilities = createAgeScores(products);
//
//        // 10개의 상품으을추출한다.
//        List<Integer> tenIndexs = rouletteAlgorithm(products, Probabilities);
//
//        // age -= 1
//        ageManagement(products);
//
//        // age를 부여한다.
//        ageGrant(tenIndexs, Probabilities, products);
//
//        // DTO를 만들어서 보낸다.
//        return createRedisDTO(tenIndexs, products);
//    }
//
//    public List<Double> createAgeScores(List<ProductDTO> products){
//        List<Double> ageScores = new ArrayList<Double>();
//
//        //각각의 가중치와 해당 값들을 구하여 더한 것이 ageScore
//        for(ProductDTO p : products){
//            double ageScore = weights[0]*p.getDailyAdImpressions() +
//                    weights[1]*p.getDetailsProductClickCount() +
//                    weights[2]*p.getTotalViewers();
//            ageScores.add(ageScore);
//            sum += ageScore;
//        }
//
//        //룰렛 휠 알고리즘을 위해 필요한 확률로 만들어준다.
//        for(int i = 0; i < ageScores.size(); i++){
//            ageScores.set(i, ageScores.get(i)/sum);
//            // maximum
//            if(ageScores.get(i) > max){
//                max = ageScores.get(i);
//            }
//        }
//
//        return ageScores;
//    }
//
//    public List<Integer> rouletteAlgorithm(List<ProductDTO> products, List<Double> ageScores){
//        //확률들을 누적시켜 더한 값을 리스트로 저장해둔다.
//        List<Integer> tenIndexs = new ArrayList<>();
//        List<Double> cumulativeProbabilities = new ArrayList<>();
//        double cumulativeSum = 0;
//
//        for (double probability : ageScores) {
//            cumulativeSum += probability;
//            cumulativeProbabilities.add(probability);
//        }
//
//        //상품 선택
//        Random random = new Random();
//        while(tenIndexs.size() < 10){
//            double r = random.nextDouble();
//            //0에서 1사이의 랜덤한 실수를 정하고, 그 실수보다 큰 한가지를 골라서 저장.
//            for(int i = 0; i < cumulativeProbabilities.size(); i++){
//                if (r <= cumulativeProbabilities.get(i)){
//                    tenIndexs.add(i);
//                    break;
//                }
//            }
//        }
//
//        return tenIndexs;
//    }
//
//    public void ageManagement(List<ProductDTO> products){
//        for(ProductDTO p : products){
//            Integer age = p.getAge();
//            if(age>0){
//                p.changeAge(age-1);
//            }
//        }
//    }
//
//
//    // 에이지 부여
//    public void ageGrant(List<Integer> tenIndexs, List<Double> probabilities, List<ProductDTO> products){
//        //최댓값을 5로 두고, 20%씩 낮추면서 4,3,2,1 이런식으로 부여할 예정
//        for(Integer i : tenIndexs){
//            double ratio = probabilities.get(i)/max;
//
//            if(ratio > 0.8){
//                products.get(i).changeAge(5);
//            } else if (ratio > 0.6) {
//                products.get(i).changeAge(4);
//            } else if (ratio > 0.4) {
//                products.get(i).changeAge(3);
//            } else if (ratio > 0.2) {
//                products.get(i).changeAge(2);
//            } else {
//                products.get(i).changeAge(1);
//            }
//
//        }
//    }
//
//
//
//    public RedisDTO createRedisDTO(List<Integer> tenIndexs, List<ProductDTO> products){
//        List<Long> productIds = new ArrayList<>();
//        List<ProductDTO> productDtos = new ArrayList<>();
//        List<RedisProduct> redisProducts = new ArrayList<>();
//        List<RedisShop> redisShops = new ArrayList<>();
//        List<RedisDTO> redisDTO = new ArrayList<>();
//
////        public class RedisDTO {
////            private Long mall_id;
////            private String mall_name;
////            private List<ShopDTO> shop_list;
////        }
//
//        //shop을 가져오기 위한 productID를 가져오는 반복문, 10개의 product를 저장하는 코드
//        for(Integer i : tenIndexs){
//            ProductDTO p = products.get(i);
//            productIds.add(p.getId());
//            productDtos.add(p);
//        }
//
//        // shop 정보가지고오기
//        List<ShopDTO> shops = createShopDTO(productIds);
//
//        for(int i = 0; i < Mall.values().length ; i++){
//            // return 1
//            // return 2
//        }
//
//
//
//    }
//
//    public List<ProductDTO> createProducts(){
//
//        Optional<List<ProductDTO>> products = repositoryFactory.getProductRepository().findAllAge();
//
//        if(products.isPresent()){
//            return products.get();
//        }else{
//            log.info("DB Product 데이터 추출 실패");
//            throw new NoSuchElementException("No products found");
//        }
//
//    }
//
//    public List<ShopDTO> createShopDTO(List<Long> productId){
//        Optional<List<ShopDTO>> shops = repositoryFactory.getShopRepository().findByProduct(productId);
//        if(shops.isPresent()){
//            return shops.get();
//        }else{
//            log.info("DB Shop 데이터 추출 실패");
//            throw new NoSuchElementException("No products found");
//        }
//    }
//}
