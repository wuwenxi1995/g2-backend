package org.g2.scheduler.infra.util;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
public final class RpcClient {

    private static final Logger log = LoggerFactory.getLogger(RpcClient.class);

    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    private RpcClient() {
    }

    private static Call buildCall(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(jsonParams, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return okHttpClient.newCall(request);
    }

    public static String execute(String url, String jsonParams) throws Exception {
        Call call = buildCall(url, jsonParams);
        Response response = call.execute();

        String result = "";
        try {
            if (response.code() == 404) {

            }
            if (!response.isSuccessful()) {
                throw new RuntimeException("");
            }
            if (response.body() != null) {
                result = response.body().string();
            }
            return result;
        } catch (Exception e) {
            log.error("");
            throw e;
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                log.error("");
                e.addSuppressed(e);
            }
        }
    }


}
