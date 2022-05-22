package com.yu.week03.gatWay.filter;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ProxyBizFiter extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if(!fullHttpRequest.uri().equals("/")){
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FORBIDDEN);
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            return;
//            fullHttpRequest.setUri("/");
        }
        fullHttpRequest.headers().set("leafw", "abc");
        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
