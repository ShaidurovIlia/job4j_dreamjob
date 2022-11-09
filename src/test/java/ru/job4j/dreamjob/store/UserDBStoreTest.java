package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

class UserDBStoreTest {
    private static UserDBStore userStore;

    @BeforeEach
    void before() throws SQLException {
        new Main().loadPool().getConnection().prepareStatement("delete from users").execute();
        userStore = new UserDBStore(new Main().loadPool());
    }

    @Test
    void findAll() {
        User user1 = new User(1, "a", "a@a.a", "a", LocalDateTime.now());
        User user2 = new User(2, "b", "b@b.b", "b", LocalDateTime.now());
        userStore.addUser(user1);
        userStore.addUser(user2);
        List<User> all = userStore.findAll();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    void addUser() {
        User user = new User(0, "a", "a@a.a", "a", LocalDateTime.now());
        userStore.addUser(user);
        Assertions.assertEquals(1, userStore.findAll().size());
        Assertions.assertTrue(userStore.findAll().contains(user));
    }

    @Test
    void findByIdUser() {
        User user1 = new User(1, "a", "a@a.a", "a", LocalDateTime.now());
        User user2 = new User(2, "b", "b@b.b", "b", LocalDateTime.now());
        userStore.addUser(user1);
        userStore.addUser(user2);
        Assertions.assertEquals(userStore.findByIdUser(user1.getId()), user1);
        Assertions.assertEquals(userStore.findByIdUser(user2.getId()), user2);
        Assertions.assertNull(userStore.findByIdUser(3));
    }

    @Test
    void findByEmailUser() {
        User user1 = new User(1, "a", "a@a.a", "a", LocalDateTime.now());
        User user2 = new User(2, "b", "b@b.b", "b", LocalDateTime.now());
        userStore.addUser(user1);
        userStore.addUser(user2);
        Assertions.assertEquals(userStore.findByEmailUser(user1.getEmail()), user1);
        Assertions.assertEquals(userStore.findByEmailUser(user2.getEmail()), user2);
        Assertions.assertNotNull(userStore.findByEmailUser("a@a.a"));
        Assertions.assertNull(userStore.findByEmailUser("c@c.c"));
    }

    @Test
    void findByNameUser() {
        User user1 = new User(1, "a", "a@a.a", "a", LocalDateTime.now());
        User user2 = new User(2, "b", "b@b.b", "b", LocalDateTime.now());
        userStore.addUser(user1);
        userStore.addUser(user2);
        Assertions.assertEquals(userStore.findByNameUser(user1.getName()), user1);
        Assertions.assertEquals(userStore.findByNameUser(user2.getName()), user2);
        Assertions.assertNotNull(userStore.findByNameUser("a"));
        Assertions.assertNull(userStore.findByNameUser("c"));
    }

    @Test
    void updateUser() {
        User user1 = new User(1, "a", "a@a.a", "a", LocalDateTime.now());
        User user2 = new User(2, "b", "b@b.b", "b", LocalDateTime.now());
        userStore.addUser(user1);
        user2.setId(user1.getId());
        userStore.updateUser(user2);
        Assertions.assertEquals(userStore.findByIdUser(user2.getId()), user2);
    }
}