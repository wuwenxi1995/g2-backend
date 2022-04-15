package org.g2.inv.app.service.impl;

import com.groupon.uuid.UUID;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.util.Operation;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.inv.api.dto.ApiResponse;
import org.g2.inv.app.service.ApiIdempotentService;
import org.g2.inv.infra.InvApiConstants;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-04-15
 */
@Service
public class ApiIdempotentServiceImpl implements ApiIdempotentService {

    private static final String API_IDEMPOTENT_TOKEN_KEY = "g2inv:idempotent:token:%s";

    private final DynamicRedisHelper dynamicRedisHelper;

    public ApiIdempotentServiceImpl(DynamicRedisHelper dynamicRedisHelper) {
        this.dynamicRedisHelper = dynamicRedisHelper;
    }

    @Override
    public String token() {
        return operation(() -> {
            String token = new UUID().toString();
            // 5分分钟失效
            dynamicRedisHelper.strSet(String.format(API_IDEMPOTENT_TOKEN_KEY, token), token, 5, TimeUnit.MINUTES);
            return token;
        });
    }

    @Override
    public <T> ApiResponse<T> idempotentOperation(String token, Supplier<T> supplier) {
        if (StringUtils.isBlank(token)) {
            return ApiResponse.error(InvApiConstants.ApiResponseCode.INVALID_PARAM);
        }
        return operation(() -> {
            Boolean success = dynamicRedisHelper.getRedisTemplate().delete(String.format(API_IDEMPOTENT_TOKEN_KEY, token));
            if (success == null || !success) {
                return ApiResponse.error(InvApiConstants.ApiResponseCode.INVALID_TOKE);
            }
            return ApiResponse.ok(supplier.get());
        });
    }

    private <T> T operation(Supplier<T> supplier) {
        dynamicRedisHelper.setCurrentDataBase(1);
        try {
            return supplier.get();
        } finally {
            dynamicRedisHelper.clearCurrentDataBase();
        }
    }
}
