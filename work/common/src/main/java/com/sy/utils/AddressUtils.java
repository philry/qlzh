package com.sy.utils;



import com.sy.config.Global;
import com.sy.json.JSON;
import com.sy.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * 获取地址类
 * 
 * @author ruoyi
 */
public class AddressUtils
{
//    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);
    private static final Logger log = Logger.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getRealAddressByIP(String ip)
    {
        String address = "XX XX";

        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        if (Global.isAddressEnabled())
        {
            String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
            if (StringUtils.isEmpty(rspStr))
            {
            //    log.error("获取地理位置异常 {}", ip);
                log.error("获取地理位置异常 {}"+ ip);
                return address;
            }
            JSONObject obj;
            try
            {
                obj = JSON.unmarshal(rspStr, JSONObject.class);
                JSONObject data = obj.getObj("data");
                String region = data.getStr("region");
                String city = data.getStr("city");
                address = region + " " + city;
            }
            catch (Exception e)
            {
//                log.error("获取地理位置异常 {}", ip);
                log.error("获取地理位置异常 {}"+ ip);
            }
        }
        return address;
    }
}
