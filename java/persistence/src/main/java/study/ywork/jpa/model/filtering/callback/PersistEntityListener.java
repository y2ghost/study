package study.ywork.jpa.model.filtering.callback;

import javax.persistence.PostPersist;

public class PersistEntityListener {
    @PostPersist
    public void notifyAdmin(Object entityInstance) {
        User currentUser = CurrentUser.INSTANCE.get();
        Mail mail = Mail.INSTANCE;
        mail.send(
                "Entity instance persisted by "
                        + currentUser.getUsername()
                        + ": "
                        + entityInstance
        );
    }
}
