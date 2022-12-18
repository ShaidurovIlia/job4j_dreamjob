package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PostDBStoreTest {

    @Test
    public void whenCreatePostThenFindById() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Developer", "Java Job", "21.10.2011", true);
        store.addPost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenCreatePostThenFindAll() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(1, "Developer", "Java Job", "21.10.2011", true);
        Post postTwo = new Post(2, "Developer", "C++ Job", "06.06.2014", true);
        store.addPost(post);
        store.addPost(postTwo);
        List<Post> expected = List.of(post, postTwo);
        List<Post> allPost = store.findAllPosts();
        List<Post> rsl = allPost.subList(allPost.size() - 2, allPost.size());
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenUpdatePostThenFindById() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Developer", "Java Job", "21.10.2011", true);
        store.addPost(post);
        Post postTwo = new Post(post.getId(), "Developer", "C++ Job", "06.06.2014", true);
        store.update(postTwo);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb, is(postTwo));
    }
}