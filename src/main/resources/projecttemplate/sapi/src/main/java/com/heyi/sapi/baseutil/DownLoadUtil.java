package com.heyi.${project.dataBase}.baseutil;

import com.heyi.${project.dataBase}.basecommon.BaseException;
import okhttp3.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.FAILED;

public class DownLoadUtil {
    public static InputStream download(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        try {
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .connectTimeout(40000, TimeUnit.MILLISECONDS)
                    .readTimeout(40000 * 10, TimeUnit.MILLISECONDS)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new BaseException(FAILED, "通信失败url:" + request.url());
            }
            ResponseBody body = response.body();
            return body.byteStream();
        } catch (IOException e) {
            throw new BaseException(FAILED, "通信失败url:" + request.url());
        }
    }
}
