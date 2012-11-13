package org.inigma.shared.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

public class MongoMessageSource extends AbstractMessageSource {
    @Autowired
    protected MongoOperations mongo;
    private String collection = "message";

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setMongo(MongoOperations mongo) {
        this.mongo = mongo;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Stack<String> stack = buildLocaleStack(locale);
        Message message;
        while (!stack.isEmpty()) {
            String localeString = stack.pop();
            message = findMessage(code, localeString);
            if (message != null) {
                return new MessageFormat(message.getValue(), locale);
            }
        }
        return null;
    }

    private Stack<String> buildLocaleStack(Locale locale) {
        String localeString;
        Stack<String> stack = new Stack<String>();
        stack.push(null);
        if (locale == null) {
            return stack;
        }

        if (StringUtils.hasText(locale.getLanguage())) {
            localeString = locale.getLanguage();
            stack.push(localeString);
            if (StringUtils.hasText(locale.getCountry())) {
                localeString += "_" + locale.getCountry();
                stack.push(localeString);
                if (StringUtils.hasText(locale.getVariant())) {
                    localeString += locale.getVariant();
                    stack.push(locale.getVariant());
                }
            }
        }
        return stack;
    }

    private Message findMessage(String code, String locale) {
        return mongo.findOne(Query.query(Criteria.where("code").is(code).and("locale").is(locale)), Message.class,
                collection);
    }
}