package org.inigma.shared.message;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.inigma.shared.webapp.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController extends BaseController {
    @Autowired
    private MessageDaoTemplate template;

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(@ModelAttribute Message message, HttpServletResponse response) {
        if (!validateMessage(message)) {
            response(response, null);
            return;
        }
        Message deleted = template.delete(message.getCode(), message.getLocale());
        response(response, deleted);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public void getMessage(@ModelAttribute Message message, HttpServletResponse response) {
        if (!validateMessage(message)) {
            response(response, null);
            return;
        }
        Message found = template.findById(message.getCode(), message.getLocale());
        response(response, found);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public void getMessages(HttpServletResponse response) {
        response(response, template.find());
    }

    @RequestMapping(value = "/message", method = { RequestMethod.POST, RequestMethod.PUT })
    public void updateMessage(@ModelAttribute Message message, HttpServletResponse response) {
        ValidationUtils.rejectIfEmptyOrWhitespace(getErrors(), "value", "required");
        if (!validateMessage(message)) {
            response(response, null);
            return;
        }
        template.save(message);
        response(response, message);
    }

    private boolean validateMessage(Message message) {
        ValidationUtils.rejectIfEmptyOrWhitespace(getErrors(), "code", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(getErrors(), "locale", "required");
        if (message.getLocale() != null) {
            String[] tokens = message.getLocale().split("_", 3);
            Locale locale = new Locale(tokens[0]);
            switch (tokens.length) {
            case 1:
                locale = new Locale(tokens[0]);
                break;
            case 2:
                locale = new Locale(tokens[0], tokens[1]);
                break;
            case 3:
                locale = new Locale(tokens[0], tokens[1], tokens[2]);
                break;
            }
            message.setLocale(locale.toString());
        }
        return hasNoErrors();
    }
}
