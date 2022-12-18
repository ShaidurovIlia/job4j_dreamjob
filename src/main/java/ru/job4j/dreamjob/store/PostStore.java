package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
@Repository
/**
 * Слой персистенции.
 * Модель данных.
 * Класс хранилище вакансий Post в памяти.
 */
public class PostStore {

    private static final AtomicInteger POST_ID =  new AtomicInteger(4);

    private AtomicReference<String> date = new AtomicReference<>(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

    /**
     * Внутреняя карта для работы с многопоточностью.
     */

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "desc Junior",  date.get(), true));
        posts.put(2, new Post(2, "Middle Java Job", "desc Middle", date.get(), true));
        posts.put(3, new Post(3, "Senior Java Job", "desc Senior", date.get(), true));
    }

    /**
     * Геттер всех значений хранилища
     * @return Collection<Post>
     */
    public List<Post> findAll() {
        return (List<Post>) posts.values();
    }

    /**
     * Метод добавляет post во внутреннее
     * хранилище posts(map) по ключу.
     * @param post
     */

    public void create(Post post) {
        post.setId(POST_ID.getAndIncrement());
        date = new AtomicReference<>(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        post.setCreated(date.get());
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    /**
     * Обновляет запись PostStore.
     * старая запись по id меняеться на
     * переданый post.
     * @param post
     */

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }
}