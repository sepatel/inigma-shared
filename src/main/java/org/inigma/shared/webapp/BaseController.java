package org.inigma.shared.webapp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

/**
 * Base controller providing access to a common set of functionality.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class BaseController {
    protected ModelAndView error(String code) {
        getErrors().reject(code);
        return new ModelAndView("error");
    }

    protected ModelAndView error(String param, String code) {
        getErrors().rejectValue(param, code);
        return new ModelAndView("error");
    }

    protected Errors getErrors() {
        Errors errors = (Errors) RequestContextHolder.getRequestAttributes().getAttribute("errors",
                RequestAttributes.SCOPE_REQUEST);
        if (errors == null) {
            throw new IllegalStateException("Errors not properly initialized in the request attributes!");
        }
        return errors;
    }

    protected String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    protected boolean validateNotBlank(String key, HttpServletRequest request) {
        if (!StringUtils.hasText(request.getParameter(key))) {
            getErrors().rejectValue(key, "blank");
            return false;
        }
        return true;
    }

    protected void stopOnValidationErrors() {
        if (getErrors().hasErrors()) {
            throw new ValidationException(getErrors());
        }
    }

    protected ModelAndView view(String view) {
        if (getErrors().hasErrors()) {
            return new ModelAndView("error");
        }
        return new ModelAndView(view);
    }

    protected ModelAndView view(String view, Map<String, ?> model) {
        if (getErrors().hasErrors()) {
            return new ModelAndView("error");
        }
        return new ModelAndView(view, model);
    }

    protected ModelAndView view(String view, String name, Object model) {
        if (getErrors().hasErrors()) {
            return new ModelAndView("error");
        }
        return new ModelAndView(view, name, model);
    }
}
