package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PostDBStoreTest {

    private static PostDBStore postStore;

    @BeforeEach
    void before() throws SQLException {
        new Main().loadPool().getConnection().prepareStatement("delete from post").execute();
        postStore = new PostDBStore(new Main().loadPool());
    }

    @Test
    void findAll() {
        Post post1 = new Post(1, "a", "a", LocalDateTime.now(), new City(1, "a"));
        Post post2 = new Post(2, "b", "b", LocalDateTime.now(), new City(2, "b"));
        postStore.addPost(post1);
        postStore.addPost(post2);
        List<Post> all = postStore.findAll();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    void addPost() {
        Post post1 = new Post(1, "a", "a", LocalDateTime.now(), new City(1, "a"));
        postStore.addPost(post1);
        Assertions.assertEquals(1, postStore.findAll().size());
        Assertions.assertTrue(postStore.findAll().contains(post1));
    }
}