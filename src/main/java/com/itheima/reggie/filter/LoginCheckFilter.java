package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.ThreadUserIdGetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public final AntPathMatcher matcher = new AntPathMatcher();
    public static String[] checkList = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/frontend/**",
            "/backend/page/demo/**",
            "/user/sendMsg",
            "/user/login",
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String URI = request.getRequestURI();
        long threadId = Thread.currentThread().getId();
        log.info("current session id: {}", threadId);
        // 不需要过滤的清单
        if (checkValidURI(URI)) {
            log.info("本次请求不需要处理: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // 用户session非空，不过滤
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录: {}", request.getSession().getAttribute("employee"));
            String employeeId = (String) request.getSession().getAttribute("employee");
            ThreadUserIdGetter.setCurrentId(employeeId);
            filterChain.doFilter(request, response);
            return;
        }

        // 客户session非空，不过滤
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录: {}", request.getSession().getAttribute("user"));
            String userId = (String) request.getSession().getAttribute("user");
            ThreadUserIdGetter.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("拦截到请求: {}", request.getRequestURI());
        return;
    }

    private boolean checkValidURI(String URI) {
        for (int i = 0; i < checkList.length; i++) {
            if (matcher.match(checkList[i], URI))
                return true;
        }
        return false;
    }
}
