package com.sy.constant;

/**
 * 用于定义Http状态码的常量类
 */
public class HttpStatusConstant {
    /**
     * 200:通用成功状态码
     **/
    public static final int SUCCESS = 200;
    /**
     * 401:登录失败状态码
     **/
    public static final int LOGIN_FAILURE = 401;
    /**
     * 403:访问权限不足状态码
     **/
    public static final int ACCESS_DENY = 403;
    /**
     * 404:访问失败状态码
     **/
    public static final int FAIL = 404;
}
