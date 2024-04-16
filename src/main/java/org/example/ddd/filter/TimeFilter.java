package org.example.ddd.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class TimeFilter implements Filter {

    @Value("${urls:/hello/world}")
    private List<String> urlPatterns;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("=======初始化过滤器=========");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String contextPath = req.getContextPath();
        log.info("contextPath = {}", contextPath);
        String requestUri = req.getRequestURI().replaceFirst(contextPath, "");
        log.info("before doFilter。。。");
        chain.doFilter(request, response);
        for (String uriItem : urlPatterns){
            if(requestUri.contains(uriItem)){
                log.info("执行了doFilter逻辑");
                chain.doFilter(request,response);
                return;
            }
        }
        log.info("after doFilter。。。");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("=======销毁过滤器=========");
    }
}
