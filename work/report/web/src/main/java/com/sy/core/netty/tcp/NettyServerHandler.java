package com.sy.core.netty.tcp;

import com.sy.core.netty.tcp.util.BytesUtils;
import com.sy.core.netty.tcp.util.CRC16Util;
import com.sy.core.netty.tcp.util.ClientChannel;
import com.sy.dao.NettyDao;
import com.sy.entity.Netty;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelHandlerAdapter {

	protected final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	private static Map<String, String> map = new ConcurrentHashMap<>();

	@Autowired
	private NettyDao nettyDao;

	private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		channels.add(ctx.channel());// 加入ChannelGroup
		// logger.info(">>>>>>>"+ctx.channel().id()+"有TCP连接成功,"+"当前活跃连接数:
		// "+channels.size());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext context) {
		// logger.info(">>>>>>>"+context.channel().id()+"有TCP连接断开,"+"当前活跃连接数:
		// "+channels.size());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);// 复制内容到字节数组bytes
		String receiveStr = BytesUtils.bytes2HexString(bytes);// 将接收到的数据转为字符串，此字符串就是客户端发送的字符串
		logger.info(">>>>>>>>获取到的hex数据为："+receiveStr);

		logger.info(">>>>>>>>获取到的hex数据为："+receiveStr);

		String isrReg4gStr = receiveStr.substring(0, 6);//截取7b7bXX的开头，XX表示具体的命令字：如 84 ，91 ，92

		byte[] mlz = Arrays.copyOfRange(bytes, 2, 3);// 命令字

		int cmd = BytesUtils.getInt(mlz);

		// 返回16进制到客户端
		String returnHexStr = "7b7b84bf237d7d";
		// 设备注册
		if (isrReg4gStr.equals("7b7b84")) { //原来的写法是：cmd == 132 ，这里面有问题，主要是接口到的报文会有00db84的情况
			byte[] xsf = Arrays.copyOfRange(bytes, 0, 2);// 起始符
			// logger.info(">>>>>>>>起始符为："+BytesUtils.getString(xsf));

			byte[] zcxlh = Arrays.copyOfRange(bytes, 3, 23);// 注册序列号
			String zcxlhStr = BytesUtils.getString(zcxlh);
			logger.info(">>>>>>>>获取到的注册序列号为：" + zcxlhStr);

			// 保存会话，通过4g注册号
			ClientChannel.addChannel(zcxlhStr.trim(), ctx.channel());
			;
			map.put(ctx.channel().id().asLongText(), zcxlhStr.trim());

			byte[] kh = Arrays.copyOfRange(bytes, 23, 53);// 卡号
			// logger.info(">>>>>>>>卡号为："+BytesUtils.getString(kh));

			byte[] dssjjg = Arrays.copyOfRange(bytes, 60, 61);// 定时上传间隔
			// logger.info(">>>>>>>>定时上传间隔为："+BytesUtils.getInt(dssjjg));

			returnHexStr = "7b7b84bf237d7d";
		}

		// 报警设定值字段上传
		if (cmd == 137) {

		}

		// 对时命令
		if (cmd == 147) {

		}
		// modbus命令回传，返回的是请求原报文
		if (cmd == 144) {

		}


		// Modbus数据域上传
		if (isrReg4gStr.equals("7b7b91")) {

			System.out.println("收到数据信息");

			Netty netty = new Netty();
			netty.setCreateTime(new Timestamp(new Date().getTime()));

			String xpg = map.get(ctx.channel().id().asLongText());
			netty.setXpg(xpg);//存放4G注册码
			netty.setRemark(receiveStr);//存放原报文

			//定义电流获取位置
			String iStart = "312D33";

			int iStartIndex = receiveStr.indexOf(iStart);
			String iInfo = receiveStr.substring(iStartIndex+10,iStartIndex+730);

			System.out.println(iInfo);

			List<Float> floats = new ArrayList<>();

			for (int i = 0; i <60 ; i++) {
				int sIndex = i*12;
				String str = iInfo.substring(sIndex+0,sIndex+4);
				float iA = BytesUtils.hexString2Float(str);
				floats.add(iA);
			}

			String currents = floats.toString().substring(1,floats.toString().length()-1);

			netty.setCurrents(currents);

			nettyDao.save(netty);

			returnHexStr = "7b7b84bf237d7d";

		}

		writeToClient(returnHexStr, ctx);

		Thread.sleep(1000);


	}

	private void writeToClient(final String receiveStr, ChannelHandlerContext channel) {
		try {
			ByteBuf bufff = Unpooled.buffer();// netty需要用ByteBuf传输
			bufff.writeBytes(BytesUtils.hexString2Bytes(receiveStr));// 对接需要16进制
			channel.writeAndFlush(bufff).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("AAAA回写成功:" + receiveStr);
					} else {
						System.out.println("AAAA回写失败:" + receiveStr);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用通用writeToClient()异常" + e.getMessage());
		}
	}

	private void writeToClient(final String receiveStr, Channel channel) {
		try {
			ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
			bufff.writeBytes(BytesUtils.hexString2Bytes(receiveStr));//对接需要16进制
			channel.writeAndFlush(bufff).addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("BBBB回写成功:"+receiveStr);
					} else {
						System.out.println("BBBB回写失败:"+receiveStr);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用通用writeToClient()异常"+e.getMessage());
		}
	}

	private String genCmdMsg(String slaveId , String offset , boolean isopen){
		String hex16 = slaveId+"0500"+offset;
		if(isopen){
			hex16 =  hex16 + "ff00"; //打开
		}else{
			hex16 =  hex16 + "0000"; //关闭
		}
		byte[] modbusBytes = BytesUtils.hexString2Bytes(hex16);
		String modbusCrc16 = CRC16Util.getCRC(modbusBytes);

		hex16 = hex16+modbusCrc16;//生成modbus指令
		hex16 = "90"+ hex16;

		byte[] tcpBytes = BytesUtils.hexString2Bytes(hex16);
		String tcpCrc16 = CRC16Util.getCRC(tcpBytes);
		hex16 = "7b7b"+hex16+tcpCrc16+"7d7d";

		return hex16;
	}

	private void closejidianqi() {
		String xp4g = "";
		String kgwzm = "";//开关位置
		String yztbh = "";//格式：1,2

		Channel channel = ClientChannel.getChannel(xp4g.trim());
		if(channel == null){
			logger.info("连接暂未建立,请稍后再试...");
		}
		if(!channel.isActive()){
			logger.info("连接已断开，请等待建立连接...");
		}

		String hexString = genCmdMsg(kgwzm,yztbh.split(",")[1],true);
		logger.info(">>>>>>>关闭继电器AAA---命令报文为："+hexString);

		writeToClient(hexString,channel);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(ctx.channel().id() + " 有连接异常断开," + "当前活跃连接数: " + channels.size());
		System.out.println(cause.getMessage());
		ctx.close();
	}
}