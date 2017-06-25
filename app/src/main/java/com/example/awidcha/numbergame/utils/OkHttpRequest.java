package com.example.awidcha.numbergame.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Song.cpe on 24/6/2560.
 */

public class OkHttpRequest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Message HttpPostMessage(String url, String json) throws IOException {
        Message message = new Message();
        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(Constant.HTTP.CONNECTION_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .build();


        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request
                .Builder()
                .post(body)
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful()) {
                message.obj = response.body().string();
                message.what = 1;
            } else {
                message.obj = response.body().string();
                message.what = 0;
            }
            return message;
        }
    }
}
