package com.sy.core.netty.tcp.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientChannel {

	
	private static Map<String, Channel> map = new ConcurrentHashMap<>();

	private static Map<String, ChannelHandlerContext> channelMap = new ConcurrentHashMap<>();

	public static void addChannel(String id, Channel channel){
	        map.put(id, channel);
	    }

	public static Map<String, Channel> getChannels(){
	        return map;
	    }

	public static Channel getChannel(String id){
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
