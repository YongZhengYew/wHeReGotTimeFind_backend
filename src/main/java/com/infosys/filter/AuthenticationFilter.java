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

        String authToken = req.getParameter("authToken");

        boolean isTryingToLoginOrSignUp = (
                (req.getRequestURI().equals("/login") || req.getRequestURI().equals("/signup"))
                &&
                req.getMethod().equals("POST")
        );

        // If trying to log in or signup, let the request through regardless of authorization
        if (isTryingToLoginOrSignUp) {
            chain.doFilter(request, response);
        } else if (authToken == null) { // else, block if no authToken present
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.reset();
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else { // if authToken is present, then validate authorization
            String username = req.getParameter("username");
            String storedAuthToken = userService.getTempAuthTokenByName(username);

            // block if authToken is invalid
            if (storedAuthToken == null || !storedAuthToken.equals(authToken)) {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.reset();
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // else let request through
            chain.doFilter(request, response);
        }
    }
}
