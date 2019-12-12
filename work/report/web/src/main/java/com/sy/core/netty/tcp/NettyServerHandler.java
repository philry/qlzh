package com.sy.core.netty.tcp;

import com.sy.core.netty.tcp.util.BytesUtils;
import com.sy.core.netty.tcp.util.ClientChannel;
import com.sy.dao.NettyDao;
import com.sy.entity.Netty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
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
		if (cmd == 145) {

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


	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(ctx.channel().id() + " 有连接异常断开," + "当前活跃连接数: " + channels.size());
		ctx.close();
	}

	public static void main(String[] args) {

		String receiveStr = "7B7B915B5B312D31282801036000050000000008900000000000000000000000000000000000000000000003E803E803E803E8000100010000004A00000000000000000000004A0000000000007530000100000000E10F0F7F00000000130C09010E302605792E0D1A0B0000008DA729295D5D5B5B312D32282801030605792E0D1A0B6EA629295D5D5B5B312D33282800000000000700000000000800000000000900000000000A00000000000B00000000000C00000000000D00000000000E00000000000F00000000001000000000001100000000001200000000001300000000001400000000001500000000001600000000001700000000001800000000001900000000001A00000000001B00000000001C00000000001D00000000001E00000000001F00000000002000000000002100000000002200000000002300000000002400000000002500000000002600000000002700000000002800000000002900000000002A00000000002B00000000002C00000000002D00000000002E00000000002F00000000003000000000003100000000003200000000003300000000003400000000003500000000003600000000003700000000003800000000003900000000003A00000000003B00000000000500000000000500000000000500000000000500000000000500000000000500000000000529295D5D27017D7D";

//		//定义电流获取位置
//		String iStart = "312D33";
//
//		int iStartIndex = receiveStr.indexOf(iStart);
//		String iInfo = receiveStr.substring(iStartIndex+10,iStartIndex+730);
//
//		System.out.println(iInfo);
//
//		List<Float> floats = new ArrayList<>();
//
//		for (int i = 0; i <60 ; i++) {
//			int sIndex = i*12;
//			String str = iInfo.substring(sIndex+0,sIndex+4);
//			float iA = BytesUtils.hexString2Float(str);
//			floats.add(iA);
//		}
//
//		String currents = floats.toString().substring(1,floats.toString().length()-1);




	}



}