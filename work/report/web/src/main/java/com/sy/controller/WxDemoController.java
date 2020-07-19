package com.sy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sy.constant.CodeConstants;
import com.sy.utils.Sha1;
import com.sy.utils.WxHttpUtils;
import com.sy.vo.JsonResult;
import com.sy.wx.WxPayUtil;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/wx")
public class WxDemoController {

    @RequestMapping(value = "/wx", method = RequestMethod.GET)
    public @ResponseBody
    JsonResult getWxPayUtil(@RequestParam(value = "url", required = true) String url)
            throws Exception {
        try {
            Map<String, String> resultMap = new HashMap<String, String>();

            String jsapiTicket = "";

            /**get token  */
            String TOKEN =
           WxHttpUtils.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                   + CodeConstants.wxappid + "&secret=" + CodeConstants.wxappsecrect);

            // P25w2GWRmAu9SRyuTADIJUUgzYmg2HBjFVRiMP7G4uL

            Map<String, String> maps = JSONObject.parseObject(TOKEN, HashMap.class);

            if (maps.get("access_token") != null) {


                jsapiTicket = WxHttpUtils.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" +  maps.get("access_token")
                        + "&type=jsapi");

                maps = JSONObject.parseObject(jsapiTicket, HashMap.class);

                jsapiTicket = maps.get("ticket");


                String t = WxPayUtil.payTimestamp();
                String n = WxPayUtil.getNonceStr();
                resultMap.put("timestamp", t);
                resultMap.put("noncestr", n);
                resultMap.put("url", url);
                resultMap.put("jsapi_ticket", jsapiTicket);
                Collection<String> keyset = resultMap.keySet();

                List list = new ArrayList<String>(keyset);

                Collections.sort(list);
                String string1 = "";
                // 这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
                for (int i = 0; i < list.size(); i++) {
                    string1 += list.get(i) + "=" + resultMap.get(list.get(i)) + "&";
                }
                string1 = string1.substring(0, string1.length() - 1);

                System.out.println(string1);
                // 这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
                // String msp = WxpubOAuth.httpBuildQuery(resultMap);//sha1加密
                String signature = Sha1.getSha1(string1);
                // url
                resultMap.put("timestamp", t);
                resultMap.put("noncestr", n);
                resultMap.put("signature", signature);
                resultMap.put("appId", "" + CodeConstants.wxappid);



            }

            return JsonResult.buildSuccess(200, JSON.toJSONString(resultMap));
        }
        catch (Exception e)
        {

          throw e ;
        }

    }
}
