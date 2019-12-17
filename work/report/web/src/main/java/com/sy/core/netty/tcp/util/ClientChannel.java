package com.sy.core.netty.tcp.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientChannel {
	
	 	private static Map<String, Channel> map = new ConcurrentHashMap<>();
	    
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
	    
	    
	    
	    
	    
}
