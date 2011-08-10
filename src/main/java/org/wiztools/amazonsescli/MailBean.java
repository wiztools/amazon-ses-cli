package org.wiztools.amazonsescli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author subWiz
 */
class MailBean {
    private String from;
    private final List<String> to = new ArrayList<String>();
    
    private String subject;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public void addTo(String to) {
        this.to.add(to);
    }
    
    public List<String> getTo() {
        return Collections.unmodifiableList(to);
    }
}
