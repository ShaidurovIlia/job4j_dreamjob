package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDBStore;

import java.util.Optional;

@ThreadSafe
@Service
public class UserService {
    private final UserDBStore userDBStore;

    public UserService(UserDBStore store) {
        this.userDBStore = store;
    }

    /**
     * добавляет user в userDBStore
     * @param user
     * @return Optional<User>
     */
    public Optional<User> add(User user) {
        return userDBStore.add(user);
    }
}
