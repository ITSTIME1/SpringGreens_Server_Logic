package com.spring_greens.logic.global.factory.service.ifs;

// import com.spring_greens.logic.auth.service.UserService;
import com.spring_greens.logic.extraction.service.ExtractionServiceImpl;
// import com.spring_greens.logic.fcm.service.FcmService;
// import com.spring_greens.logic.global.redis.service.RedisService;
// import com.spring_greens.logic.mall.service.ifs.MallService;
// import com.spring_greens.logic.shop.service.ShopService;

public interface ServiceFactory {
    ExtractionServiceImpl getExtractionService();
}
