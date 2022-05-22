package com.yu.week03;

import com.yu.week03.gatWay.handler.HttpInboundInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

//@SpringBootApplication
public class Week03Application {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(16);
//		SpringApplication.run(Week03Application.class, args);
		String proxyPort = System.getProperty("proxyPort","8088");
		String proxyServers = System.getProperty("proxyServers","http://localhost:8081,http://localhost:8082");
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)
					.childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
					.childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
					.childOption(EpollChannelOption.SO_REUSEPORT, true)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			int port = Integer.parseInt(proxyPort);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG))
					.childHandler(new HttpInboundInitializer(Arrays.asList(proxyServers.split(","))));

			Channel ch = b.bind(port).sync().channel();
			System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
