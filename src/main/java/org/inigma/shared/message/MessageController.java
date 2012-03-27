package org.inigma.shared.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.inigma.shared.webapp.BaseController;
import org.inigma.shared.webapp.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController extends BaseController {
    @Autowired
    private MessageDaoTemplate template;

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    @ResponseBody
    public RestResponse deleteMessage(MessageResponse message) {
        validateMessage(message);
        return new MessageResponse(template.delete(message.getCode(), message.getLocale()));
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getMessage(MessageResponse message) {
        validateMessage(message);
        return new MessageResponse(template.findById(message.getCode(), message.getLocale()));
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public Collection<? extends RestResponse> getMessages() {
        Collection<RestResponse> responses = new ArrayList<RestResponse>();
        for (Message msg : template.find()) {
            responses.add(new MessageResponse(msg));
        }
        return responses;
    }

    @RequestMapping(value = "/message", method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseBody
    public RestResponse updateMessage(MessageResponse message) {
        Errors errors = getErrors(message, "message");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "required");
        validateMessage(message, errors);
        template.save(new Message(message));
        return message;
    }

    private void validateMessage(MessageResponse message) {
        validateMessage(message, null);
    }

    private void validateMessage(MessageResponse message, Errors errors) {
        if (errors == null) {
            errors = getErrors(message, "message");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "required");
        if (message.getLocale() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "locale", "blank");
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
        stopOnErrors(errors);
    }
}
