package com.sy.core.netty.tcp.util;

import org.apache.commons.lang.ArrayUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 字节数组转换工具类
 */
public class BytesUtils {

    public static final String GBK = "GBK";
    public static final String UTF8 = "utf-8";
    public static final char[] ascii = "0123456789ABCDEF".toCharArray();
    private static char[] HEX_VOCABLE = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * 将short整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((data & 0xff00) >> 8);
        bytes[1] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将字符转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data >> 8);
        bytes[1] = (byte) (data);
        return bytes;
    }

    /**
     * 将布尔值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(boolean data) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (data ? 1 : 0);
        return bytes;
    }

    /**
     * 将整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((data & 0xff000000) >> 24);
        bytes[1] = (byte) ((data & 0xff0000) >> 16);
        bytes[2] = (byte) ((data & 0xff00) >> 8);
        bytes[3] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将long整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((data >> 56) & 0xff);
        bytes[1] = (byte) ((data >> 48) & 0xff);
        bytes[2] = (byte) ((data >> 40) & 0xff);
        bytes[3] = (byte) ((data >> 32) & 0xff);
        bytes[4] = (byte) ((data >> 24) & 0xff);
        bytes[5] = (byte) ((data >> 16) & 0xff);
        bytes[6] = (byte) ((data >> 8) & 0xff);
        bytes[7] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将float型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    /**
     * 将double型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    /**
     * 将字符串按照charsetName编码格式的字节数组
     * 
     * @param data
     *            字符串
     * @param charsetName
     *            编码格式
     * @return
     */
    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    /**
     * 将字符串按照GBK编码格式的字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(String data) {
        return getBytes(data, GBK);
    }

    /**
     * 将字节数组第0字节转换为布尔值
     * 
     * @param bytes
     * @return
     */
    public static boolean getBoolean(byte[] bytes) {
        return bytes[0] == 1;
    }

    /**
     * 将字节数组的第index字节转换为布尔值
     * 
     * @param bytes
     * @param index
     * @return
     */
    public static boolean getBoolean(byte[] bytes, int index) {
        return bytes[index] == 1;
    }

    /**
     * 将字节数组前2字节转换为short整型数值
     * 
     * @param bytes
     * @return
     */
    public static short getShort(byte[] bytes) {
        return (short) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
    }

    /**
     * 将字节数组从startIndex开始的2个字节转换为short整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static short getShort(byte[] bytes, int startIndex) {
        return (short) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
    }

    /**
     * 将字节数组前2字节转换为字符
     * 
     * @param bytes
     * @return
     */
    public static char getChar(byte[] bytes) {
        return (char) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
    }

    /**
     * 将字节数组从startIndex开始的2个字节转换为字符
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static char getChar(byte[] bytes, int startIndex) {
        return (char) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
    }

    /**
     * 将字节数组前4字节转换为整型数值
     * 
     * @param bytes
     * @return
     */
    public static int getInt(byte[] bytes) {
    	int aaa = 0;
    	if(bytes.length == 4){
    		 aaa= (0xff000000 & (bytes[0] << 24) | (0xff0000 & (bytes[1] << 16))
    	                | (0xff00 & (bytes[2] << 8)) | (0xff & bytes[3]));
    	}
    	if(bytes.length == 2){
   		 aaa= (0xff000000 & (bytes[0] << 24) | (0xff0000 & (bytes[1] << 16))
	                );
    	}
    	if(bytes.length == 1){
   		 	aaa= ((0xff & bytes[0]));
    	}
    	return aaa;
    }

    /**
     * 将单字节转成int
     * @param b
     * @return
     */
    public static int byte2Int(byte b){
        return (int)(b & 0xff);
    }
    
    /**
     * 将字节数组从startIndex开始的4个字节转换为整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static int getInt(byte[] bytes, int startIndex) {
        return (0xff000000 & (bytes[startIndex] << 24)
                | (0xff0000 & (bytes[startIndex + 1] << 16))
                | (0xff00 & (bytes[startIndex + 2] << 8)) | (0xff & bytes[startIndex + 3]));
    }

    /**
     * 将字节数组前8字节转换为long整型数值
     * 
     * @param bytes
     * @return
     */
    public static long getLong(byte[] bytes) {
        return (0xff00000000000000L & ((long) bytes[0] << 56)
                | (0xff000000000000L & ((long) bytes[1] << 48))
                | (0xff0000000000L & ((long) bytes[2] << 40))
                | (0xff00000000L & ((long) bytes[3] << 32))
                | (0xff000000L & ((long) bytes[4] << 24))
                | (0xff0000L & ((long) bytes[5] << 16))
                | (0xff00L & ((long) bytes[6] << 8)) | (0xffL & (long) bytes[7]));
    }

    /**
     * 将字节数组从startIndex开始的8个字节转换为long整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static long getLong(byte[] bytes, int startIndex) {
        return (0xff00000000000000L & ((long) bytes[startIndex] << 56)
                | (0xff000000000000L & ((long) bytes[startIndex + 1] << 48))
                | (0xff0000000000L & ((long) bytes[startIndex + 2] << 40))
                | (0xff00000000L & ((long) bytes[startIndex + 3] << 32))
                | (0xff000000L & ((long) bytes[startIndex + 4] << 24))
                | (0xff0000L & ((long) bytes[startIndex + 5] << 16))
                | (0xff00L & ((long) bytes[startIndex + 6] << 8)) | (0xffL & (long) bytes[startIndex + 7]));
    }

    /**
     * 将字节数组前4字节转换为float型数值
     * 
     * @param bytes
     * @return
     */
    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    /**
     * 将字节数组从startIndex开始的4个字节转换为float型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static float getFloat(byte[] bytes, int startIndex) {
        byte[] result = new byte[4];
        System.arraycopy(bytes, startIndex, result, 0, 4);
        return Float.intBitsToFloat(getInt(result));
    }

    /**
     * 将字节数组前8字节转换为double型数值
     * 
     * @param bytes
     * @return
     */
    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将字节数组从startIndex开始的8个字节转换为double型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static double getDouble(byte[] bytes, int startIndex) {
        byte[] result = new byte[8];
        System.arraycopy(bytes, startIndex, result, 0, 8);
        long l = getLong(result);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将charsetName编码格式的字节数组转换为字符串
     * 
     * @param bytes
     * @param charsetName
     * @return
     */
    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    /**
     * 将GBK编码格式的字节数组转换为字符串
     * 
     * @param bytes
     * @return
     */
    public static String getString(byte[] bytes) {
        return getString(bytes, GBK);
    }

    /**
     * 将16进制字符串转换为字节数组
     * 
     * @param hex
     * @return
     */
   
    public static byte[] hexString2Bytes(String hexStr) {
		int index = 0;
		if (hexStr == null || hexStr.equals("")) {
			return null;
		}
		if (hexStr.startsWith("0x", index) || hexStr.startsWith("0X", index)) {
			index += 2;
		}
		hexStr = hexStr.substring(index).toUpperCase();
		int length = hexStr.length() / 2;
		char[] hexChars = hexStr.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (toByte(hexChars[pos]) << 4 | toByte(hexChars[pos + 1]));
		}
		return d;
	}
    /** 
     * @Title:hexString2String 
     * @Description:16进制字符串转字符串 
     * @param src 
     *            16进制字符串 
     * @throws 
     */  
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp+ (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),16).byteValue();
        }  
        return temp; 
    } 
    
    /**
     * 将hexString变成数字
     * @param src
     * @return
     */
    public static Integer hexString2Int(String src) {
        return Integer.parseInt(src, 16);
    } 
    
    /**
     * 将hexString变成数字
     * @param src
     * @return
     */
    public static Float hexString2Float(String src) {
    	byte[] bytes = hexString2Bytes(src);
    	Float f = getFloat(bytes);
    	return f;
    	//BigDecimal b = new BigDecimal(f);  
		//return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
    	/*if(f < 0){
    		return 0f;
    	}else{
    		
    	}*/
    	//return getFloat(bytes);
    }
    
/*    public static Double hexString2Double(String src) { 
    	byte[] bytes = hexString2Bytes(src);
    	return getDouble(bytes);
    } */
    
    /**
     * 16进制转2进制
     * @param hexString
     * @return
     */
    public static String hexString2BinaryString(String hexString) {
        int num = Integer.parseInt(hexString, 16);
        return Integer.toBinaryString(num);
    } 
    
    
    /** 
     * @Title:string2HexString 
     * @Description:字符串转16进制字符串 
     * @param strPart  字符串 
     * @return 16进制字符串 
     */  
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {  
            int ch = (int) strPart.charAt(i);  
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);  
        }  
        return hexString.toString();  
    }
    
    /**
     *  Convert byte[] to hex string.
	 * @param src byte[] data   
	 * @return hex string   
	 */   
    public static String bytes2HexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v =  0xFF & src[i] ;   
            String hv = Integer.toHexString(v);
            if(hv.length()==1){//如果只有一位，需要补齐全两位
      		  hv = "0" + hv;
            }
            stringBuilder.append(hv);   
        }   
        
        return stringBuilder.toString();   
    }


    /**
     * 将16进制字符串转换为字节数组
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0)
            throw new IllegalArgumentException(
                    "input string should be any multiple of 2!");
        hex.toUpperCase();

        byte[] byteBuffer = new byte[hex.length() / 2];

        byte padding = 0x00;
        boolean paddingTurning = false;
        for (int i = 0; i < hex.length(); i++) {
            if (paddingTurning) {
                char c = hex.charAt(i);
                int index = indexOf(hex, c);
                padding = (byte) ((padding << 4) | index);
                byteBuffer[i / 2] = padding;
                padding = 0x00;
                paddingTurning = false;
            } else {
                char c = hex.charAt(i);
                int index = indexOf(hex, c);
                padding = (byte) (padding | index);
                paddingTurning = true;
            }

        }
        return byteBuffer;
    }

    private static int indexOf(String input, char c) {
        int index = ArrayUtils.indexOf(HEX_VOCABLE, c);

        if (index < 0) {
            throw new IllegalArgumentException("err input:" + input);
        }
        return index;

    }

    /**
     * 将BCD编码的字节数组转换为字符串
     * 
     * @param bcds
     * @return
     */
    public static String bcdToString(byte[] bcds) {
        if (bcds == null || bcds.length == 0) {
            return null;
        }
        byte[] temp = new byte[2 * bcds.length];
        for (int i = 0; i < bcds.length; i++) {
            temp[i * 2] = (byte) ((bcds[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bcds[i] & 0x0f);
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString();
    }

    /**
     * 字节转整形
     * @param value
     * @return
     */
    public static int bcdToInt(byte value){
        return ((value>>4) * 10) + (value&0x0F);
    }
    
    /**
     * 字节数组转16进制字符串
     * @param bs
     * @return
     */
    public static String bytesToHex(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    
    /**
     * 字节数组取前len个字节转16进制字符串
     * @param bs
     * @param len
     * @return
     */
    public static String bytesToHex(byte[] bs, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++ ) {
            byte b = bs[i];
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    /**
     * 字节数组偏移offset长度之后的取len个字节转16进制字符串
     * @param bs
     * @param offset
     * @param len
     * @return
     */
    public static String bytesToHex(byte[] bs, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++ ) {
            byte b = bs[offset + i];
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    /**
     * 字节数组转16进制字符串
     * @param bs
     * @return
     */
    public static String byteToHex(byte b) {
        StringBuilder sb = new StringBuilder();
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        return sb.toString();
    }
    
    /**
     * 将字节转换成二进制
     * @param b
     * @return
     */
    public static String byteToBitString(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
    /**
     * 将字节数组取反
     * 
     * @param src
     * @return
     */
    public static String negate(byte[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        byte[] temp = new byte[2 * src.length];
        for (int i = 0; i < src.length; i++) {
            byte tmp = (byte) (0xFF ^ src[i]);
            temp[i * 2] = (byte) ((tmp >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (tmp & 0x0f);
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString();
    }

    /**
     * 比较字节数组是否相同
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean compareBytes(byte[] a, byte[] b) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length != b.length) {
            return false;
        }
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    /**
     * 只比对指定长度byte
     * @param a
     * @param b
     * @param len
     * @return
     */
    public static boolean compareBytes(byte[] a, byte[] b, int len) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length < len || b.length < len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字节数组转换为二进制字符串
     * 
     * @param items
     * @return
     */
    public static String bytesToBinaryString(byte[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (byte item : items) {
            buf.append(byteToBinaryString(item));
        }
        return buf.toString();
    }

    /**
     * 将字节转换为二进制字符串
     * 
     * @param items
     * @return
     */
    public static String byteToBinaryString(byte item) {
        byte a = item;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            buf.insert(0, a % 2);
            a = (byte) (a >> 1);
        }
        return buf.toString();
    }

    /**
     * 对数组a，b进行异或运算
     * 
     * @param a
     * @param b
     * @return
     */
    public static byte[] xor(byte[] a, byte[] b) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length != b.length) {
            return null;
        }
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    /**
     * 对数组a，b进行异或运算 运算长度len
     * @param a
     * @param b
     * @param len
     * @return
     */
    public static byte[] xor(byte[] a, byte[] b, int len) {
        if (a == null || a.length == 0 || b == null || b.length == 0) {
            return null;
        }
        if (a.length < len || b.length < len){
            return null;
        }
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
    /**
     * 将short整型数值转换为字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] shortToBytes(int num) {
        byte[] temp = new byte[2];
        for (int i = 0; i < 2; i++) {
            temp[i] = (byte) ((num >>> (8 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转为整型
     * 
     * @param num
     * @return
     */
    public static int bytesToShort(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 2; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将整型数值转换为指定长度的字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] intToBytes(int num) {
        byte[] temp = new byte[4];
        for (int i = 0; i < 4; i++) {
            temp[i] = (byte) ((num >>> (24 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将整型数值转换为指定长度的字节数组
     * 
     * @param src
     * @param len
     * @return
     */
    public static byte[] intToBytes(int src, int len) {
        if (len < 1 || len > 4) {
            return null;
        }
        byte[] temp = new byte[len];
        for (int i = 0; i < len; i++) {
            temp[len - 1 - i] = (byte) ((src >>> (8 * i)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转换为整型数值
     * 
     * @param arr
     * @return
     */
    public static int bytesToInt(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将long整型数值转换为字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] longToBytes(long num) {
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
            temp[i] = (byte) ((num >>> (56 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转换为long整型数值
     * 
     * @param arr
     * @return
     */
    public static long bytesToLong(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        long result = 0;
        int len = Math.min(8, arr.length);
        for (int i = 0; i < len; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将16进制字符转换为字节
     * 
     * @param c
     * @return
     */
    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    
    /**
     * 功能描述：把两个字节的字节数组转化为整型数据，高位补零，例如：<br/>
     * 有字节数组byte[] data = new byte[]{1,2};转换后int数据的字节分布如下：<br/>
     * 00000000  00000000 00000001 00000010,函数返回258
     * @param lenData 需要进行转换的字节数组
     * @return  字节数组所表示整型值的大小
     */
    public static int bytesToIntWhereByteLengthEquals2(byte lenData[]) {
        if(lenData.length != 2){
            return -1;
        }
        byte fill[] = new byte[]{0,0};
        byte real[] = new byte[4];
        System.arraycopy(fill, 0, real, 0, 2);
        System.arraycopy(lenData, 0, real, 2, 2);
        int len = byteToInt(real);
        return len;
        
    }
    
    /**
     * 功能描述：将byte数组转化为int类型的数据
     * @param byteVal 需要转化的字节数组
     * @return 字节数组所表示的整型数据
     */
    public static int byteToInt(byte[] byteVal) {
        int result = 0;
        for(int i = 0;i < byteVal.length;i++) {
            int tmpVal = (byteVal[i]<<(8*(3-i)));
            switch(i) {
                case 0:
                    tmpVal = tmpVal & 0xFF000000;
                    break;
                case 1:
                    tmpVal = tmpVal & 0x00FF0000;
                    break;
                case 2:
                    tmpVal = tmpVal & 0x0000FF00;
                    break;
                case 3:
                    tmpVal = tmpVal & 0x000000FF;
                    break;
            }
        
            result = result | tmpVal;
        }
        return result;
    }
    public static byte CheckXORSum(byte[] bData){
        byte sum = 0x00;
        for (int i = 0; i < bData.length; i++) {
            sum ^= bData[i];
        }
        return sum;
    }
    /**
     * 从offset开始 将后续长度为len的byte字节转为int
     * @param data
     * @param offset
     * @param len
     * @return
     */
    public static int bytesToInt(byte[] data, int offset, int len){
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        len = Math.min(len, 4);
        for (int i = 0; i < len; i++) {
            result <<= 8;
            temp = data[offset + i] & mask;
            result |= temp;
        }
        return result;
    }
    
    /**
     * byte字节数组中的字符串的长度
     * @param data
     * @return 
     */
    public static int getBytesStringLen(byte[] data)
    {
        int count = 0;
        for (byte b : data) {
            if(b == 0x00)
                break;
            count++;
        }
        return count;
    }
    
    public static String get8BitReverseBinaryStringFromHexString(String hexString) {
    	StringBuilder ori = new StringBuilder(hexString2BinaryString(hexString));
    	ori.reverse();
    	while(ori.length() < 8) {
    		ori.append("0");
    	}
    	return ori.toString();
    }

    public static int covert(String content){
        int number=0;
        String [] HighLetter = {"A","B","C","D","E","F"};
        Map<String,Integer> map = new HashMap<>();
        for(int i = 0;i <= 9;i++){
            map.put(i+"",i);
        }
        for(int j= 10;j<HighLetter.length+10;j++){
            map.put(HighLetter[j-10],j);
        }
        String[]str = new String[content.length()];
        for(int i = 0; i < str.length; i++){
            str[i] = content.substring(i,i+1);
        }
        for(int i = 0; i < str.length; i++){
            number += map.get(str[i])*Math.pow(16,str.length-1-i);
        }
        return number;
    }

    /**
     * float类型转换成16进制
     * @param changeData
     * @return
     */
    public static String hexadecimal(float changeData){
        return Integer.toHexString(Float.floatToIntBits(changeData));
    }

    /**
     *16进制转换为float类型
     * @param changeData
     * @return
     */
    public static float hexToTen(String changeData){
        return Float.intBitsToFloat(Integer.parseInt(changeData,16));
    }
}