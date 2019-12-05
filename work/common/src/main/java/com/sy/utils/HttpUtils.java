package com.sy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Http操作相关的工具类
 */
public class HttpUtils {
    /**
     * 私有构造，防止外部直接实例化
     */
    private HttpUtils() {

    }


    /**
     * 用于写出JSON格式的数据（此时的状态码使用的是默认状态码）
     *
     * @param res  响应对象
     * @param data 要写出成JSON格式的对象
     */
    public static void writeJson(HttpServletResponse res, Object data) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = res.getWriter();) {
            out.print(new ObjectMapper().writeValueAsString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于写出JSON格式的数据并设置状态码（RESTful风格中有时需要自己设置状态码，而不是使用默认的）
     *
     * @param res  响应对象
     * @param data 要写出成JSON格式的数据
     * @param code 要设置的状态码
     */
    public static void writeJson(HttpServletResponse res, Object data, int code) {
        res.setStatus(code);
        writeJson(res, data);

    }



}
