package com.sy.core.netty.tcp.util;

/**
 * CRC16位工具类
 * @author administrator
 *
 */
public class CRC16Util {
	
	
	public static String getCRC(byte[] bytes) {
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;

		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		String hexString = Integer.toHexString(CRC);
		if (hexString.length() == 3) {
			hexString = "0" + hexString;
		}
		if (hexString.length() == 2) {
			hexString = "00" + hexString;
		}
		if (hexString.length() == 1) {
			hexString = "000" + hexString;
		}
		String retunHexString = hexString.substring(2, 4) + hexString.substring(0, 2);

		return retunHexString;
	}

}