package org.inigma.shared.message;

import java.util.Collection;

public interface MessageDaoTemplate {
    Message delete(String code, String locale);

    Collection<Message> find();

    Message findById(String code, String locale);

    void save(Message message);
}
