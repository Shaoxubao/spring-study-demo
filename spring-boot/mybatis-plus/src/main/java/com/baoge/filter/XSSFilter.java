package com.baoge.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "xssFilter", urlPatterns = "/*", asyncSupported = true)
public class XSSFilter implements Filter {
    /**
     * 忽略权限检查的url地址
     */
    private final String[] excludeUrls = new String[]{
            "/login.html"
    };

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        //获取请求你ip后的全部路径
        String uri = req.getRequestURI();
        //跳过不需要的Xss校验的地址
        for (String str : excludeUrls) {
            if (uri.contains(str)) {
                arg2.doFilter(arg0, response);
                return;
            }
        }
        //注入xss过滤器实例
        XssHttpServletRequestWrapper reqW = new XssHttpServletRequestWrapper(req);
        //过滤
        arg2.doFilter(reqW, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig1) throws ServletException {
    }

}

