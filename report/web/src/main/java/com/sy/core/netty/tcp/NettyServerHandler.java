package com.sy.core.netty.tcp;

import com.sy.core.netty.tcp.util.BytesUtils;
import com.sy.core.netty.tcp.util.CRC16Util;
import com.sy.core.netty.tcp.util.ClientChannel;
import com.sy.dao.*;
import com.sy.entity.Netty;
import com.sy.entity.NettyReturn;
import com.sy.service.MessageDataService;
import com.sy.utils.DateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelHandlerAdapter {

	Logger logger = Logger.getLogger(NettyServerHandler.class);
//	Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	private static Map<String, String> map = new ConcurrentHashMap<>();

	@Autowired
	private NettyDao nettyDao;

	@Autowired
	private NettyReturnDao nettyReturnDao;

	@Autowired
	private XpgMapper xpgMapper;

	@Autowired
	private MachineMapper machineMapper;

	@Autowired
	private MachineDao machineDao;

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
		channels.add(ctx.channel());
		// "+channels.size());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext context) {
		// "+channels.size());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	}

	@Override
//	@Transactional
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long startTime = System.currentTimeMillis();
		String startDateStr = dateformat.format(startTime);

		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String receiveStr = BytesUtils.bytes2HexString(bytes);

		String isrReg4gStr = receiveStr.substring(0, 6);

		byte[] mlz = Arrays.copyOfRange(bytes, 2, 3);

		int cmd = BytesUtils.getInt(mlz);


		String returnHexStr = "7b7b84bf237d7d";

		if (isrReg4gStr.equals("7b7b84")) {
			byte[] xsf = Arrays.copyOfRange(bytes, 0, 2);

			byte[] zcxlh = Arrays.copyOfRange(bytes, 3, 23);
			String zcxlhStr = BytesUtils.getString(zcxlh);


			ClientChannel.addChannel(zcxlhStr.trim(), ctx.channel());


			map.put(ctx.channel().id().asLongText(), zcxlhStr.trim());

			byte[] kh = Arrays.copyOfRange(bytes, 23, 53);

//			byte[] dssjjg = Arrays.copyOfRange(bytes, 60, 61);

			returnHexStr = "7b7b84bf237d7d";
		}


		/*if (cmd == 137) {//isrReg4gStr.equals("7b7b89")

		}*/


		/*if (isrReg4gStr.equals("7b7b93")) {
//			Calendar calendar = Calendar.getInstance();
//			int year = calendar.get(Calendar.YEAR);
//			int month = calendar.get(Calendar.MONTH)+1;
//			int day = calendar.get(Calendar.DATE);
//			int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
//			int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			int minute = calendar.get(Calendar.MINUTE);
//			int second = calendar.get(Calendar.SECOND);
//
//			String s = "93"+Integer.toHexString(Integer.parseInt(String.valueOf(year).substring(2,4)))+
//					Integer.toHexString(month)+
//					Integer.toHexString(day)+
//					Integer.toHexString(week)+
//					Integer.toHexString(hour)+
//					Integer.toHexString(minute)+
//					Integer.toHexString(second);
//
//			byte[] modbusBytes = BytesUtils.hexString2Bytes(s);
//			String modbusCrc16 = CRC16Util.getCRC(modbusBytes);
//
//			returnHexStr = "7b7b"+s+modbusCrc16+"7d7d";
		}*/

		if (isrReg4gStr.equals("7b7b90")) {

			returnHexStr = receiveStr;


			Date now = new Date();
			String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
			NettyReturn nettyReturn = new NettyReturn();
			nettyReturn.setDate(DateUtils.parseDate(today));
			nettyReturn.setCreateTime(new Timestamp(new Date().getTime()));

			String xpg = map.get(ctx.channel().id().asLongText());
			nettyReturn.setXpg(xpg);
			nettyReturn.setRemark(receiveStr);
			nettyReturnDao.save(nettyReturn);//

		}


		if (isrReg4gStr.equals("7b7b91")) {
			if(receiveStr.length()>0){
				Date now = new Date();
				String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

				String errorinfoStart = "756e";
				if(!receiveStr.contains(errorinfoStart)){
					Netty netty = new Netty();
					netty.setDate(DateUtils.parseDate(today));
					netty.setCreateTime(new Timestamp(new Date().getTime()));

					String xpg = map.get(ctx.channel().id().asLongText());
					netty.setXpg(xpg);
					netty.setRemark(receiveStr);



					String infoStart = "010360";

					int vStartIndex = receiveStr.indexOf(infoStart);

					String info = receiveStr.substring(vStartIndex+2,vStartIndex+202);
					double voltage = getDoubleValue(info.substring(8,12));

					if(voltage>100){
						String iStart = "312d33";

						int iStartIndex = receiveStr.indexOf(iStart);
						String iInfo = receiveStr.substring(iStartIndex+10,iStartIndex+730);

						List<Double> doubles = new ArrayList<>();

						//				Double maxA = null;
						//				Xpg xpg2 = new Xpg();
						//				xpg2.setName(xpg);
						//				Integer deptId = null;
						//				Integer machineId = null;
						//				List<Xpg> xpgList = xpgMapper.selectXpgList(xpg2);
						//				if(xpgList!=null&&xpgList.size()>0) {
						//					Machine machine = new Machine();
						//					machine.setXpgId(xpgList.get(0).getId());
						//					List<Machine> machineList = machineMapper.selectMachineList(machine);
						//					if(machineList!=null&&machineList.size()>0) {
						//						machineId = machineList.get(0).getId();
						//						maxA = machineList.get(0).getMaxA();
						//						deptId = machineList.get(0).getDeptId();
						//					}
						//				}

						//				boolean flag = true;

						for (int i = 0; i <60 ; i++) {
							int sIndex = i*12;
							String str = iInfo.substring(sIndex+0,sIndex+4);
							double iA  = getPowerValue(str);
							//					if(flag&maxA!=null&&iA<maxA) {
							//						flag=false;
							//					}
							doubles.add(iA);
						}

						//				if(flag) {
						//					MessageData messageData = new MessageData();
						//					messageData.setSendId(0);
						//					Integer leader = deptMapper.selectDeptById(deptId).getLeader();
						//					messageData.setAccpetId(leader);
						//					messageData.setContext(machineId.toString());
						//					messageDataService.sendMessage(messageData, 2);
						//					machineNowMapper.deleteMachineNowByMachineId(machineId);
						//					controlMachine(xpg, false);
						//				}

						String currents = doubles.toString().substring(1,doubles.toString().length()-1);

						netty.setCurrents(currents);


						netty.setVoltage(voltage);


						double power = getPowerValue(info.substring(88,96));

						netty.setPower(String.valueOf(power));

						/*
						Integer machineId = xpgMapper.selectXpgByName(xpg).getMachineId();
						String machineName = machineDao.getNameById(machineId);
						netty.setMachineName(machineName);*/

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
				}

			}

			returnHexStr = "7b7b917eec7d7d";

		}

		if(isrReg4gStr.equals("7b7b91") ||isrReg4gStr.equals("7b7b84")){
			writeToClient(returnHexStr, ctx);

			Thread.sleep(1000);
		}

		long endTime=System.currentTimeMillis();
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
			ByteBuf bufff = Unpooled.buffer();
			bufff.writeBytes(BytesUtils.hexString2Bytes(receiveStr));
			channel.writeAndFlush(bufff).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
					} else {
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void writeToClient(final String receiveStr, Channel channel) {
		try {
			ByteBuf bufff = Unpooled.buffer();
			bufff.writeBytes(BytesUtils.hexString2Bytes(receiveStr));
			channel.writeAndFlush(bufff).addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
					} else {
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String genCmdMsg(String slaveId , String offset , boolean isopen){
		String hex16 = slaveId+"0500"+offset;
		if(isopen){
			hex16 =  hex16 + "ff00";
		}else{
			hex16 = hex16 + "0000";
		}
		byte[] modbusBytes = BytesUtils.hexString2Bytes(hex16);
		String modbusCrc16 = CRC16Util.getCRC(modbusBytes);

		hex16 = hex16+modbusCrc16;
		hex16 = "90"+ hex16;

		byte[] tcpBytes = BytesUtils.hexString2Bytes(hex16);
		String tcpCrc16 = CRC16Util.getCRC(tcpBytes);
		hex16 = "7b7b"+hex16+tcpCrc16+"7d7d";

		return hex16;
	}


	public void controlMachine(String xpg,boolean isOpen) throws Exception {

		String hex16 = "01100057000204000100";

		if(isOpen){

			hex16 = hex16+"00";
		}else {

			hex16 = hex16+"01";
		}

		byte[] TcpBytes = BytesUtils.hexString2Bytes(hex16);
		String TcpCrc16 = CRC16Util.getCRC(TcpBytes);


		hex16 = "90"+ hex16+TcpCrc16;

		byte[] modbusBytes = BytesUtils.hexString2Bytes(hex16);
		String modbusCrc16 = CRC16Util.getCRC(modbusBytes);

		hex16 = "7b7b"+hex16+modbusCrc16+"7d7d";

		Channel ctx = ClientChannel.getChannel(xpg);
		if(ctx==null){
			throw new Exception(xpg);
		}

		if(!ctx.isActive()){
			throw new Exception(xpg);
		}

		writeToClient(hex16,ctx);

	}




	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}