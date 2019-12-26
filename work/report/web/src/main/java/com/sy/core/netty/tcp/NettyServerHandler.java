package com.sy.core.netty.tcp;

import com.github.pagehelper.PageHelper;
import com.sy.core.netty.tcp.util.BytesUtils;
import com.sy.core.netty.tcp.util.CRC16Util;
import com.sy.core.netty.tcp.util.ClientChannel;
import com.sy.dao.DeptMapper;
import com.sy.dao.EnergyMapper;
import com.sy.dao.MachineMapper;
import com.sy.dao.MachineNowDao;
import com.sy.dao.MachineNowMapper;
import com.sy.dao.NettyDao;
import com.sy.dao.XpgMapper;
import com.sy.entity.Energy;
import com.sy.entity.Machine;
import com.sy.entity.MessageData;
import com.sy.entity.Netty;
import com.sy.entity.Xpg;
import com.sy.service.MessageDataService;

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
import org.springframework.transaction.annotation.Transactional;

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
	
	@Autowired
	private XpgMapper xpgMapper;
	
	@Autowired
	private MachineMapper machineMapper;
	
	@Autowired
	private MachineNowDao machineNowDao;
	
	@Autowired
	private MachineNowMapper machineNowMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private MessageDataService messageDataService;
	
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
	@Transactional
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);// 复制内容到字节数组bytes
		String receiveStr = BytesUtils.bytes2HexString(bytes);// 将接收到的数据转为字符串，此字符串就是客户端发送的字符串
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
		if (isrReg4gStr.equals("7b7b93")) {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);

			String s = "93"+Integer.toHexString(Integer.parseInt(String.valueOf(year).substring(2,4)))+
					Integer.toHexString(month)+
					Integer.toHexString(day)+
					Integer.toHexString(week)+
					Integer.toHexString(hour)+
					Integer.toHexString(minute)+
					Integer.toHexString(second);

			byte[] modbusBytes = BytesUtils.hexString2Bytes(s);
			String modbusCrc16 = CRC16Util.getCRC(modbusBytes);

			returnHexStr = "7b7b"+s+modbusCrc16+"7d7d";
		}
		// modbus命令回传，返回的是请求原报文
		if (cmd == 144) {

			returnHexStr = receiveStr;
		}

		// Modbus数据域上传
		if (isrReg4gStr.equals("7b7b91")) {

			if(receiveStr.length()>500){
				Netty netty = new Netty();
				netty.setCreateTime(new Timestamp(new Date().getTime()));

				String xpg = map.get(ctx.channel().id().asLongText());
				netty.setXpg(xpg);//存放4G注册码
				netty.setRemark(receiveStr);//存放原报文

				//获取电流信息，并解析合并存储
				String iStart = "312d33";

				int iStartIndex = receiveStr.indexOf(iStart);
				String iInfo = receiveStr.substring(iStartIndex+10,iStartIndex+730);

				List<Double> doubles = new ArrayList<>();
				
				Double maxA = null;
				Xpg xpg2 = new Xpg();
				xpg2.setName(xpg);
				Integer deptId = null;
				Integer machineId = null;
				List<Xpg> xpgList = xpgMapper.selectXpgList(xpg2);
				if(xpgList!=null&&xpgList.size()>0) {
					Machine machine = new Machine();
					machine.setXpgId(xpgList.get(0).getId());
					List<Machine> machineList = machineMapper.selectMachineList(machine);
					if(machineList!=null&&machineList.size()>0) {
						machineId = machineList.get(0).getId();
						maxA = machineList.get(0).getMaxA();
						deptId = machineList.get(0).getDeptId();
					}
				}
				
				boolean flag = true;
				
				for (int i = 0; i <60 ; i++) {
					int sIndex = i*12;
					String str = iInfo.substring(sIndex+0,sIndex+4);
					double iA  = getPowerValue(str);
					if(flag&maxA!=null&&iA<maxA) {
						flag=false;
					}
					doubles.add(iA);
				}
				
				if(flag) {
					MessageData messageData = new MessageData();
					messageData.setSendId(0);
					Integer leader = deptMapper.selectDeptById(deptId).getLeader();
					messageData.setAccpetId(leader);
					messageData.setContext(machineId.toString());
					messageDataService.sendMessage(messageData, 2);
					machineNowMapper.deleteMachineNowByMachineId(machineId);
					controlMachine(xpg, false);
				}
				
				String currents = doubles.toString().substring(1,doubles.toString().length()-1);

				netty.setCurrents(currents);

				//获取电压

				String infoStart = "010360";

				int vStartIndex = receiveStr.indexOf(infoStart);

				String info = receiveStr.substring(vStartIndex+2,vStartIndex+202);
				double voltage = getDoubleValue(info.substring(8,12));

				netty.setVoltage(voltage);

				//获取电量
				double power = getPowerValue(info.substring(88,96));

				netty.setPower(String.valueOf(power));
				
				nettyDao.save(netty);
				
//				List<Energy> energyList = energyMapper.selectEnergyList();
//				Integer time = energyList.get(0).getTime();
//				PageHelper.startPage(1, time);
//				List<Netty> nettyList = nettyDao.getNettyByXpg(xpg);
//				String currents2 = nettyList.get(nettyList.size()-1).getCurrents();
//				String[] split = currents2.split(",");
//				boolean flag2 = true;
//				for (String str : split) {
//					if(flag2&Integer.valueOf(str)>minA) {
//						flag2=false;
//					}
//				}
//				if(flag2) {
//					controlMachine(xpg, false);
//				}
			}

			returnHexStr = "7b7b917eec7d7d";

		}

		writeToClient(returnHexStr, ctx);

		Thread.sleep(1000);
	}

	private double getDoubleValue(String handleStr) {
		String voltageStr = String.valueOf(BytesUtils.hexString2Int(handleStr));
		if(voltageStr.length()<=1){
			return Double.parseDouble("."+voltageStr);
		}
		return Double.parseDouble(voltageStr.substring(0,voltageStr.length()-1)+"."+voltageStr.substring(voltageStr.length()-1,voltageStr.length()));
	}

	private double getPowerValue(String powerStr) {
		String s = String.valueOf(BytesUtils.hexString2Int(powerStr));
		if(s.length()<=2){
			return Double.parseDouble("."+s);
		}
		return Double.parseDouble(s.substring(0,s.length()-2)+"."+s.substring(s.length()-2,s.length()));
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

	public void controlMachine(String xpg,boolean isOpen){

		String hex16 = "01100057000204000100";

		if(isOpen){
			//合闸 开机
			hex16 = hex16+"00";
		}else {
			//分闸 关机
			hex16 = hex16+"01";
		}

		byte[] TcpBytes = BytesUtils.hexString2Bytes(hex16);
		String TcpCrc16 = CRC16Util.getCRC(TcpBytes);


		hex16 = "90"+ hex16+TcpCrc16;

		byte[] modbusBytes = BytesUtils.hexString2Bytes(hex16);
		String modbusCrc16 = CRC16Util.getCRC(modbusBytes);

		hex16 = "7b7b"+hex16+modbusCrc16+"7d7d";

		Channel ctx = ClientChannel.getChannel(xpg);

		writeToClient(hex16,ctx);
	}




	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(ctx.channel().id() + " 有连接异常断开," + "当前活跃连接数: " + channels.size());
		System.out.println(cause.getMessage());
		ctx.close();
	}

}