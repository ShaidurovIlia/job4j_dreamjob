package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static final PostStore INST = new PostStore();

    private final AtomicInteger idS = new AtomicInteger(1);

    private final Map<Integer, Post> postStore = new ConcurrentHashMap<>();

    private PostStore() {
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return postStore.values();
    }

    public void add(Post post) {
        post.setId(idS.getAndIncrement());
        post.setCreated(LocalDateTime.now());
        postStore.put(post.getId(), post);
    }

    public Post findById(int id) {
        return postStore.get(id);
    }

    public void update(Post post) {
        postStore.replace(post.getId(), post);
    }
}
