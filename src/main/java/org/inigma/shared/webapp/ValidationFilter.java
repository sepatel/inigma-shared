package org.inigma.shared.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.validation.MapBindingResult;

public class ValidationFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain f) throws IOException, ServletException {
        MapBindingResult errors = new MapBindingResult(req.getParameterMap(), "request");
        req.setAttribute("errors", errors);
        f.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
