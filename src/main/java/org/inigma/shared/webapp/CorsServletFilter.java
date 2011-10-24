package org.inigma.shared.webapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet filter adds the headers needed to allow CORS (Cross Origin Resource Sharing) compliant browsers to be
 * able and make direct ajax calls without the need for a proxy.
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class CorsServletFilter implements Filter {
    private String accessAllowOrigin;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accessAllowOrigin = filterConfig.getInitParameter("allow-origin");
        if (accessAllowOrigin == null) {
            accessAllowOrigin = "*";
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain f) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", accessAllowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE"); // full rest support
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with"); // jquery 1.5 support

        f.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
