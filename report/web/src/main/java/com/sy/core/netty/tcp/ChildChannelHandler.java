package com.sy.core.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Resource
	private NettyServerHandler nettyServerHandler;

    public void initChannel(SocketChannel socketChannel) throws Exception {
		
		ChannelPipeline  pipeline  =  socketChannel.pipeline();

		/*pipeline.addLast(new MsgEncoder());
		pipeline.addLast(new MsgDecoder());*/

		pipeline.addLast("handler", nettyServerHandler);
    }
}