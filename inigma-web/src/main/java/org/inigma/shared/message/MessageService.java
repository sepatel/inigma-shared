package org.inigma.shared.message;

import java.util.Locale;

import org.inigma.shared.webapp.Response;
import org.inigma.shared.webapp.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <pre>db.message.ensureIndex({code: 1, locale: 1}, {unique: true})</pre>
 *
 * @author <a href="sejal@inigma.org">Sejal Patel</a>
 */
@Controller
@RequestMapping("/inigma")
public class MessageService extends RestService {
    @Autowired
    private MessageDaoTemplate template;

    @RequestMapping(value = "/message/{code}", method = RequestMethod.DELETE)
    public Response deleteMessage(@PathVariable String code) {
        return deleteMessage(code, null);
    }

    @RequestMapping(value = "/message/{code}/{locale}", method = RequestMethod.DELETE)
    public Response deleteMessage(@PathVariable String code, @PathVariable String locale) {
        validateMessage(code, locale);
        Message message = template.delete(code, locale);
        if (message == null) {
            stopImmediately("notFound");
        }
        return response(message);
    }

    @RequestMapping(value = "/message/{code}", method = RequestMethod.GET)
    public Response getMessage(@PathVariable String code) {
        logger.info("Code is {}", code);
        return getMessage(code, null);
    }

    @RequestMapping(value = "/message/{code}/{locale}", method = RequestMethod.GET)
    public Response getMessage(@PathVariable String code, @PathVariable String locale) {
        validateMessage(code, locale);
        Message message = template.findById(code, locale);
        if (message == null) {
            stopImmediately("notFound");
        }
        return response(message);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public Response getMessages() {
        return response(template.find());
    }

    @RequestMapping(value = "/message", method = { RequestMethod.POST, RequestMethod.PUT })
    public Response updateMessage() {
        Message message = getRequest(Message.class);
        rejectIfEmptyOrWhitespace("code");
        rejectIfWhitespace("locale", false);
        rejectIfEmptyOrWhitespace("value");

        message.setLocale(convertToLocale(message.getLocale()));
        template.save(message);
        return response(message);
    }

    private String convertToLocale(String locale) {
        if (locale == null) {
            return null;
        }
        String[] tokens = locale.split("_", 3);
        Locale loc = new Locale(tokens[0]);
        switch (tokens.length) {
            case 1:
                loc = new Locale(tokens[0]);
                break;
            case 2:
                loc = new Locale(tokens[0], tokens[1]);
                break;
            case 3:
                loc = new Locale(tokens[0], tokens[1], tokens[2]);
                break;
        }
        return locale.toString();
    }

    private void validateMessage(String code, String locale) {
        if (!StringUtils.hasText(code)) {
            reject("invalidCode");
        }

        if (locale != null) {
            if (!StringUtils.hasText(locale)) {
                reject("blankLocale");
            }
        }
        stopOnRejections();
    }
}
