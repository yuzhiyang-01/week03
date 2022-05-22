package com.yu.week03.gatWay.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.http.HttpClient;

import java.util.List;
import java.util.Random;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
        private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
        private final List<String> proxyServer;
        public HttpInboundHandler(List<String> proxyServer) {
            this.proxyServer = proxyServer;
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                FullHttpRequest fullRequest = (FullHttpRequest) msg;
                String uri = fullRequest.uri();
                System.out.println("uri = "+uri);
            // 随机路由到一个服务器上
            int size = proxyServer.size();
            Random random = new Random(System.currentTimeMillis());
            String url = proxyServer.get(random.nextInt(size));
            OkHttpwwk02.get(ctx,url+uri);

        }
}
