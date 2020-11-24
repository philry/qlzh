package com.sy.core.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NettyServer {
	
	@Resource
	private ChildChannelHandler childChannelHandler;

	public void run(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128).childHandler(childChannelHandler);

			ChannelFuture f = bootstrap.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {

			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}