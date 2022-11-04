package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@ThreadSafe
@Repository
public class PostStore {

    private final AtomicInteger idS = new AtomicInteger(1);

    private final Map<Integer, Post> postStore = new ConcurrentHashMap<>();

    public Collection<Post> findAll() {
        return postStore.values();
    }

    public void addPost(Post post) {
        post.setId(idS.getAndIncrement());
        post.setCreated(LocalDateTime.now());
        postStore.put(post.getId(), post);
    }

    public Post findByIdPost(int id) {
        return postStore.get(id);
    }

    public void updatePost(Post post) {
        postStore.replace(post.getId(), post);
    }
}
