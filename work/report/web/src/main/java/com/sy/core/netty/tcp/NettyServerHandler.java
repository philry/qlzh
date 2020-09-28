package com.sy.core.netty.tcp;

import com.github.pagehelper.PageHelper;
import com.sy.core.netty.tcp.util.BytesUtils;
import com.sy.core.netty.tcp.util.CRC16Util;
import com.sy.core.netty.tcp.util.ClientChannel;
import com.sy.dao.*;
import com.sy.entity.Energy;
import com.sy.entity.Machine;
import com.sy.entity.MessageData;
import com.sy.entity.Netty;
import com.sy.entity.Xpg;
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
import org.springframework.transaction.annotation.Transactional;

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

	@Override	  //正常发包给我是数据域上传(数据域上传：仪表发数据包给我，我给仪表个回复)
//	@Transactional//开关机透传时他给我的回复不用再回复他了(数据透传：我发指令给仪表，仪表给我个回复)
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = System.currentTimeMillis();
        String startDateStr = dateformat.format(startTime);
        System.out.println("channelRead程序开始时间是："+startDateStr);
        logger.info("---------[logger]channelRead程序开始时间是："+startDateStr);

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
		if (isrReg4gStr.equals("7b7b84")) { //原来的写法是：cmd == 132 ，84的16进制就是132 ，这里面有问题，主要是接到的报文会有00db84的情况
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
		/*if (cmd == 137) {//isrReg4gStr.equals("7b7b89")//十六进制89的十进制就是137

		}*/

		// 对时命令 由于会有解析失败的问题,所以暂时直接标准回复即可
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
		// 透传不用回复
		/*原来的
		if (cmd == 144) {//isrReg4gStr.equals("7b7b90") 十六进制90的十进制就是144

			returnHexStr = receiveStr;
		}*/

		// Modbus数据域上传
		if (isrReg4gStr.equals("7b7b91")) {
        //    System.out.println("-----------运行到if (isrReg4gStr.equals(\"7b7b91\"))之后一步了"+"-----------");
			logger.info("-----------运行到if (isrReg4gStr.equals(\"7b7b91\"))之后一步了"+"-----------");
			if(receiveStr.length()>0){
//                System.out.println("-----------运行到if(receiveStr.length()>0)之后一步了"+"------------");
				logger.info("-----------运行到if(receiveStr.length()>0)之后一步了"+"------------");
				//获取指定日期
				Date now = new Date();
				String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

				String errorinfoStart = "756e";//报文中含有756e就是异常报文
				if(!receiveStr.contains(errorinfoStart)){//报文中没有756e就是正常报文
//                    System.out.println("-----------运行到报文里不含756e了"+"-------------");
					logger.info("-----------运行到报文里不含756e了"+"-------------");
					Netty netty = new Netty();
					netty.setDate(DateUtils.parseDate(today));
					netty.setCreateTime(new Timestamp(new Date().getTime()));

					String xpg = map.get(ctx.channel().id().asLongText());
					netty.setXpg(xpg);//存放4G注册码
					netty.setRemark(receiveStr);//存放原报文

					//获取电压

					String infoStart = "010360";


					int vStartIndex = receiveStr.indexOf(infoStart);

					String info = receiveStr.substring(vStartIndex+2,vStartIndex+202);
					double voltage = getDoubleValue(info.substring(8,12));

					if(voltage>100){
//                        System.out.println("------------运行到if(voltage>100)之后一步了"+"------------");
						logger.info("------------运行到if(voltage>100)之后一步了"+"------------");
						//获取电流信息，并解析合并存储
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

						//获取电量
						double power = getPowerValue(info.substring(88,96));

						netty.setPower(String.valueOf(power));

						/*//加焊机编号字段               --已经在controller层实现，表不再加字段
						Integer machineId = xpgMapper.selectXpgByName(xpg).getMachineId();
						String machineName = machineDao.getNameById(machineId);
						netty.setMachineName(machineName);*/

//                        System.out.println("-----------运行到nettyDao.save方法之前一步了，netty:"+netty.getRemark()+"--------------");
						logger.info("-----------运行到nettyDao.save方法之前一步了，netty:"+netty.getRemark()+"--------------");
						nettyDao.save(netty);
//                        System.out.println("------------运行到nettyDao.save方法之后一步了,netty:"+netty.getRemark()+"-------------");
						logger.info("------------运行到nettyDao.save方法之后一步了,netty:"+netty.getRemark()+"-------------");

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

		if(isrReg4gStr.equals("7b7b93")||isrReg4gStr.equals("7b7b91") //7b7b93,7b7b91,7b7b84,7b7b89这些才要回复
				||isrReg4gStr.equals("7b7b84")||isrReg4gStr.equals("7b7b89")){
			writeToClient(returnHexStr, ctx);

			Thread.sleep(1000);
		}

        long endTime=System.currentTimeMillis();
        /*System.out.println("-------channelRead程序结束时间是："+dateformat.format(endTime)+"--------");
        System.out.println("---------channelRead程序运行时间： "+(endTime-startTime)+"ms ----------");*/
		logger.info("-------channelRead程序结束时间是："+dateformat.format(endTime)+"--------");
		logger.info("---------channelRead程序运行时间： "+(endTime-startTime)+"ms ----------");
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

	// 一般在系统自动回复中进行使用
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

	//一般在用户手动回复或者透传指令时进行使用
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
			hex16 = hex16 + "0000"; //关闭
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

	//开关机是透传(透传：我下发指令给他，他给我个回复)
	public void controlMachine(String xpg,boolean isOpen) throws Exception {

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
		System.out.println("----------------------");
		System.out.println("-----------ctx = "+ctx+"-----------");
		System.out.println("----------------------");

		if(ctx==null){
			throw new Exception(xpg+"连接尚未建立,请稍后再试");
		}

		if(!ctx.isActive()){
			throw new Exception(xpg+"连接尚未建立,请稍后再试");
		}

		writeToClient(hex16,ctx);
	}




	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(ctx.channel().id() + " 有连接异常断开," + "当前活跃连接数: " + channels.size());
		System.out.println(cause.getMessage());
		ctx.close();
	}

}