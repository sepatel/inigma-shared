package org.inigma.shared.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet filter adds the headers needed to allow CORS (Cross Origin Resource Sharing) compliant browsers to be
 * able and make direct ajax calls without the need for a proxy.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class CorsServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain f) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        String origin = request.getHeader("Origin");
        if (origin == null) {
            origin = "*";
        }
        response.setHeader("Access-Control-Allow-Origin", origin);

        String headers = request.getHeader("Access-Control-Request-Headers");
        if (headers != null) {
            response.setHeader("Access-Control-Allow-Headers", headers);
        }

        String method = request.getHeader("Access-Control-Request-Method");
        if (method != null) {
            response.setHeader("Access-Control-Allow-Methods", method); // full rest support
        }
        response.setHeader("Access-Control-Max-Age", "10"); // 10 seconds

        f.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
