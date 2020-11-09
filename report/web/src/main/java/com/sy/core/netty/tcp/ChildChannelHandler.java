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
        
        System.out.println("==================netty报告==================");
		System.out.println("信息：有一客户端链接到本服务端");
		System.out.println("IP:" + socketChannel.localAddress().getHostName());
		System.out.println("Port:" + socketChannel.localAddress().getPort());
		System.out.println("==================netty报告完毕==================");
		
		ChannelPipeline  pipeline  =  socketChannel.pipeline();
		// 在管道中添加我们自己的接收数据实现方法
		/*pipeline.addLast(new MsgEncoder());
		pipeline.addLast(new MsgDecoder());*/
		//注意 我这里并没有设置管道的编码/解码
		pipeline.addLast("handler", nettyServerHandler);//对接设备
    }
}