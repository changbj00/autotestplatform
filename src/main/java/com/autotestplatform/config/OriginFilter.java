package com.autotestplatform.config;

import com.alibaba.fastjson.JSONObject;
import com.autotestplatform.filter.XssHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
@Slf4j
@Component
@WebFilter(filterName = "OriginFilter",urlPatterns="/*")
public class OriginFilter implements Filter {

    @Value("${CROS_ALLOWED_ORIGINS}")
    private String[] allowedOrigins;

    @Value("${CROS_ALLOWED_METHOD}")
    private String allowedMethods;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;
        String originHeader=request.getHeader("Orion")!=null?request.getHeader("Origin")
                .replace(" ", ""):request.getHeader("Origin");
        log.info("header{}",originHeader);
        if (Arrays.asList(allowedOrigins).contains(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", originHeader);
            response.setHeader("Access-Control-Allow-Methods", allowedMethods);
            response.setHeader("Access-Control-Allow-Headers", "usertoken, Origin, X-Requested-With, content-Type, Accept, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true"); //是否支持cookie跨域
            response.setHeader("Access-Control-Max-Age", "3600");
        }
        log.info("CrosXssFilter.......orignal url:{},ParameterMap:{}",request.getRequestURI(), JSONObject.toJSONString(request.getParameterMap()));
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper=new XssHttpServletRequestWrapper(
                request);
        chain.doFilter(xssHttpServletRequestWrapper, resp);
        log.info("CrosXssFilter..........doFilter url:{},ParameterMap:{}",xssHttpServletRequestWrapper.getRequestURI(), JSONObject.toJSONString(xssHttpServletRequestWrapper.getParameterMap()));
    }

    @Override
    public void destroy() {

    }
}
