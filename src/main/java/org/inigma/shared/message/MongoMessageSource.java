package org.inigma.shared.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

public class MongoMessageSource extends AbstractMessageSource {
    @Autowired
    private MessageTemplate template;

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Message message = template.findById(code);
        if (message == null) {
            return null;
        }
        return new MessageFormat(message.getValue());
    }

    public void setTemplate(MessageTemplate template) {
        this.template = template;
    }
}
