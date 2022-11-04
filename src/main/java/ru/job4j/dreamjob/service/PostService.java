package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.persistence.PostStore;

import java.util.Collection;
@ThreadSafe
@Service
public class PostService {
    private final PostStore storePost;

    public PostService(PostStore store) {
        this.storePost = store;
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