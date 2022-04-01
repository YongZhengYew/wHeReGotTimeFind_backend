package com.infosys.filter;

import com.infosys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {
    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");

        String authToken = req.getParameter("authToken");
        boolean isTryingToLoginOrSignUp = (
                (req.getRequestURI().equals("/login") || req.getRequestURI().equals("/signup"))
                &&
                req.getMethod().equals("POST")
        );
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        if (authToken == null && !isTryingToLoginOrSignUp) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.reset();
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        if (isTryingToLoginOrSignUp) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        String username = req.getParameter("username");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        String storedAuthToken = userService.getTempAuthTokenByName(username);
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(storedAuthToken);
        if (storedAuthToken == null || !storedAuthToken.equals(authToken)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.reset();
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        chain.doFilter(request, response);
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
    }
}
