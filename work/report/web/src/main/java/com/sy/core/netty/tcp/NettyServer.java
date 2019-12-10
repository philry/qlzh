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
		System.out.println(">>>>>>>>>>TCP连接初始化完成，监听端口：" + port);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128).childHandler(childChannelHandler);
			// 绑定端口，同步等待成功
			ChannelFuture f = bootstrap.bind(port).sync();
			// 等待服务监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 退出，释放线程资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}