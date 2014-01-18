package org.inigma.shared.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.AbstractMessageSource;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 12/20/13 11:57 PM
 */
public class NoopMessageSource extends AbstractMessageSource {
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        return new MessageFormat(code, locale);
    }
}
