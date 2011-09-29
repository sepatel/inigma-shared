package org.inigma.shared.webapp;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Base controller providing access to a common set of functionality.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class BaseController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private MessageSource messageSource;

    protected void error(Writer w, String code) {
        getErrors().reject(code);
        response(w, null);
    }

    protected void error(Writer w, String code, String field) {
        getErrors().rejectValue(field, code);
        response(w, null);
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

    protected void response(Writer w, Object data) {
        List<ObjectError> errors = getErrors().getAllErrors();
        try {
            JSONWriter writer = new JSONWriter(w).object();
            writer.key("data").value(errors.size() == 0 ? data : null);
            writer.key("success").value(errors.size() == 0);
            writer.key("errors").array();
            for (ObjectError error : errors) {
                writer.object();
                writer.key("code").value(error.getCode());
                writer.key("message").value(messageSource.getMessage(error.getCode(), null, error.getCode(), null));
                if (error instanceof FieldError) {
                    writer.key("field").value(((FieldError) error).getField());
                }
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        } catch (JSONException e) {
            logger.error("Unable to generate response", e);
            throw new RuntimeException("Error responding with errors", e);
        }
    }

    protected boolean validateNotBlank(String key, HttpServletRequest request) {
        if (!StringUtils.hasText(request.getParameter(key))) {
            getErrors().rejectValue(key, "blank");
            return false;
        }
        return true;
    }

    protected boolean validateRequired(String key, HttpServletRequest request) {
        if (request.getParameter(key) == null) {
            getErrors().rejectValue(key, "required");
            return false;
        }
        return true;
    }
}
