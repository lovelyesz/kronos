package com.yz.kronos.client;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.config.DingConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by dustin on 2017/3/17.
 */
@Slf4j
public class DingtalkSendClient {

    private static final int timeout = 10000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;
    private static final String DING_WEBHOOK = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    public static Boolean send(DingConfig config, String title, String message) {

        StringBuilder text = new StringBuilder();
        text.append("## ").append(title).append("\n")
                .append(message);
        JSONObject body = new JSONObject();
        body.put("msgtype","markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("title",title);
        markdown.put("text",text.toString());
        body.put("markdown",markdown);

        Request request = new Request.Builder()
                .header("Content-Type", "application/json; charset=utf-8")
                .post(RequestBody.create(body.toJSONString(),MediaType.parse("application/json; charset=utf-8")))
                .url(String.format(DING_WEBHOOK, config.getToken())).build();
        Response response = null;
        try {
            response = new OkHttpClient.Builder()
                    .callTimeout(CONNECTION_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build().newCall(request)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ding send fail",e);
            return false;
        }

        return response.isSuccessful();
    }

    public static void main(String[] args) throws IOException {
        String token = "11eda069fb8e3af56e8cb50e0bf5e17bef5f5af9006d035a8b368035acb41d3c";
        String message = "测试内容";
        String title = "测试消息";
        DingConfig config = new DingConfig();
        config.setToken(token);
        Boolean send = DingtalkSendClient.send(config,title,message);
        System.out.println(send);
    }

}


