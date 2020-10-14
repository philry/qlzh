package com.sy.core.netty.tcp.util;

import com.sy.core.netty.tcp.NettyServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientChannel {

	static Logger logger = Logger.getLogger(ClientChannel.class);
	
	private static Map<String, Channel> map = new ConcurrentHashMap<>();

	private static Map<String, ChannelHandlerContext> channelMap = new ConcurrentHashMap<>();

	public static void addChannel(String id, Channel channel){
	        map.put(id, channel);
	    }

	public static Map<String, Channel> getChannels(){
	        return map;
	    }

	public static Channel getChannel(String id){
		for(String key :map.keySet()){
			logger.info("================Channel的map："+key+"=================");
		}
	        return map.get(id);
	    }
	    
	public static void removeChannel(String id){
	        map.remove(id);
	    }
	    
	public static void addChannelHandlerContext(String id, ChannelHandlerContext ctx){

		channelMap.put(id,ctx);

	}

	public static ChannelHandlerContext getChannelHandlerContext(String id){

		return channelMap.get(id);
	}
	    
	    
}
