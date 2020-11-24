package com.sy.exception.handler;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用异常处理器
 * 这里根据情况添加，如果需要就加，不加就不要放开这个@Component注解
 */
//@Component
public class CommonExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //TODO：自定义异常处理器中的处理逻辑
        return null;
    }
}
