package com.yu.week03.gatWay.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkHttpwwk02 {

    public static OkHttpClient okHttpClient = new OkHttpClient();

    public static void get(ChannelHandlerContext ctx,String url ){
        System.out.println("路径为："+url);
        Request build = new Request.Builder().url(url).build();
        try(Response response = okHttpClient.newCall(build).execute()) {
            byte[] body = response.body().bytes();
            DefaultFullHttpResponse defaultFullHttpResponse =
                    new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            defaultFullHttpResponse.headers().set("Content-Type", "application/json");
            ctx.write(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ctx.flush();
        }
    }

//    public static void main(String[] args) {
//        String url ="http://localhost:8088";
//        String message = get(url);
//        System.out.println(message);
//    }

}
