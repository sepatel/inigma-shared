package org.inigma.shared.message;

import java.util.Collection;
import java.util.Locale;

import org.inigma.shared.webapp.AjaxController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>db.message.ensureIndex({code: 1, locale: 1}, {unique: true})</pre>
 * @author <a href="sejal@inigma.org">Sejal Patel</a>
 */
@Controller
@RequestMapping("/inigma")
public class MessageController extends AjaxController {
    @Autowired
    private MessageDaoTemplate template;

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteMessage(Message message, BindingResult errors) {
        validateMessage(message, errors);
        return template.delete(message.getCode(), message.getLocale());
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ResponseBody
    public Message getMessage(Message message, BindingResult errors) {
        validateMessage(message, errors);
        return template.findById(message.getCode(), message.getLocale());
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Message> getMessages() {
        return template.find();
    }

    @RequestMapping(value = "/message", method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseBody
    public Message updateMessage(Message message, BindingResult errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "required");
        validateMessage(message, errors);
        template.save(message);
        return message;
    }

    private void validateMessage(Message message, Errors errors) {
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
