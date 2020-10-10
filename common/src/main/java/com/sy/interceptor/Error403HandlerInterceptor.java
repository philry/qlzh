package com.sy.interceptor;

import com.sy.constant.HttpStatusConstant;
import com.sy.utils.HttpUtils;
import com.sy.vo.JsonResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于处理403异常的拦截器
 */
public class Error403HandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object o, ModelAndView modelAndView) throws Exception {
        //获取状态码--Servlet3.0后的新方法
        int code = res.getStatus();
        //403状态的处理
        if (code == HttpStatusConstant.ACCESS_DENY) {
            //如果在视图返回前发现是403状态，则直接返回一个json字符串
            //包含403状态码以及提示信息
            HttpUtils.writeJson(res, JsonResult.buildFailure(HttpStatusConstant.ACCESS_DENY, "没有访问权限"), HttpStatusConstant.ACCESS_DENY);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object o, Exception e) throws Exception {

    }
}
