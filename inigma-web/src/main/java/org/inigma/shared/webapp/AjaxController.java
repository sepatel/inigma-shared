package org.inigma.shared.webapp;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base controller providing access to a common set of functionality.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
@Deprecated
public abstract class AjaxController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({ AccessDeniedException.class, InsufficientAuthenticationException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAccessDeniedException() {
        return "/error/page/401";
    }

    @ExceptionHandler({ BindException.class, RuntimeBindException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationFailureResponse handleBindException(HttpServletRequest req, BindingResult errors) {
        Locale locale = req.getLocale();
        ValidationFailureResponse failureResponse = new ValidationFailureResponse();
        for (ObjectError error : errors.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fe = (FieldError) error;
                String message = messageSource.getMessage(fe.getCode() + "." + fe.getField(), fe.getArguments(),
                        fe.getDefaultMessage(), locale);
                failureResponse.reject(((FieldError) error).getField(), error.getCode(), message);
            } else {
                String message = messageSource.getMessage(error.getCode(), error.getArguments(),
                        error.getDefaultMessage(), locale);
                failureResponse.reject(error.getCode(), message);
            }
        }
        return failureResponse;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e) {
        logger.error("Unhandled Exception", e);
        return new ExceptionResponse(e);
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected Object returnFailureResponse(String code, String message) {
        ValidationFailureResponse vfr = new ValidationFailureResponse();
        vfr.reject(code, message);
        return vfr;
    }

    protected void stopOnErrors(Errors errors) {
        if (errors.hasErrors()) {
            if (errors instanceof RuntimeBindException) {
                throw (RuntimeBindException) errors;
            } else if (errors instanceof BindingResult) {
                throw new RuntimeBindException((BindingResult) errors);
            }
            // TODO, magically make a valid runtime exception that handles errors. Not sure this case will ever happen.
            throw new IllegalStateException("Not a BindingResult Errors instance: " + errors.getClass().getName());
        }
    }
}
