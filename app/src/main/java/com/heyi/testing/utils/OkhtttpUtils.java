package com.heyi.testing.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkhtttpUtils class
 *
 * @author auser
 * @date 11/2/2019
 */
public class OkhtttpUtils {
    private static OkhtttpUtils mOkhtttpUtils;
    private OkHttpClient mOkHttpClien;

    private OkhtttpUtils() {

        /* LoggingInterceptor loggingInterceptor = new LoggingInterceptor();*/
        mOkHttpClien = new OkHttpClient.Builder()
                /* .addInterceptor(loggingInterceptor)*/
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    //单例模式
    public static OkhtttpUtils getInstance() {
        if (mOkhtttpUtils == null) {
            synchronized (OkhtttpUtils.class) {
                if (mOkhtttpUtils == null) {
                    return mOkhtttpUtils = new OkhtttpUtils();
                }
            }
        }
        return mOkhtttpUtils;
    }

    public void doPost(String url, Map<String, String> map, final OkCallback okCallback) {
        //创建FormBody的对象,把表单添加到formBody中
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (String key : map.keySet()) {
                builder.add(key, map.get(key));
            }
        }
        FormBody formBody = builder.build();

        //创建Request对象
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        //创建Call对象
        final Call call = mOkHttpClien.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (okCallback != null) {
                    okCallback.onFailure(e);
                }

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (response != null && response.isSuccessful()) {
                        final String json = response.body().string();
                        if (okCallback != null) {
                            okCallback.onResponse(json);
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (okCallback != null) {
                    okCallback.onFailure(new Exception("网络异常"));
                }
            }
        });
    }

    //封装doGet的网络请求
    public void doGet(String url, final OkCallback okCallback) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        final Call call = mOkHttpClien.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (okCallback != null) {

                    okCallback.onFailure(e);

                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                try {
                    if (response != null && response.isSuccessful()) {
                        final String json = response.body().string();
                        if (okCallback != null) {
                            okCallback.onResponse(json);
                            return;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public interface OkCallback {
        void onFailure(Exception e);

        void onResponse(String json);
    }
}
