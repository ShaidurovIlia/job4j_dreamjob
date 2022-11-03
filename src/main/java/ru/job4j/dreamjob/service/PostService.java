package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.persistence.PostStore;

import java.util.Collection;

@Service
public class PostService {
    private static final PostService INST = new PostService();

    private final PostStore storePost = PostStore.instOf();

    public static PostService instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return storePost.findAll();
    }

    public void addPost(Post candidate) {
        storePost.addPost(candidate);
    }

    public Post findByIdPost(int id) {
        return storePost.findByIdPost(id);
    }

    public void updatePost(Post candidate) {
        storePost.updatePost(candidate);
    }
}