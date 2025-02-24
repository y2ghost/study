package study.ywork.users.component;

//import org.springframework.data.relational.core.conversion.MutableAggregateChange;
//import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
//import org.springframework.stereotype.Component;
//import study.ywork.users.domain.User;
//import study.ywork.users.domain.UserRole;
//
//import java.util.List;
//
//@Component
//public class UserBeforeSaveCallback implements BeforeSaveCallback<User> {
//    @Override
//    public User onBeforeSave(User aggregate, MutableAggregateChange<User> aggregateChange) {
//        if (null == aggregate.getUserRole()) {
//            aggregate.setUserRole(List.of(UserRole.INFO));
//        }
//
//        return aggregate;
//    }
//}

// JPA不需要该类
public class UserBeforeSaveCallback {
}