package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class PostDBStore {

    private static final String FROM = "SELECT * FROM post";

    private static final String INSERT = "INSERT INTO post"
            + "(name, description, created, visible, city_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE post SET name = ?, description = ?, created = ?, visible = ?, city_id = ?"
                    + "WHERE id = ?";

    private static final String SELECT = "SELECT * FROM post WHERE id = ?";
    /**
     * Внутри объекта создаются коннекты к базе данных,
     * которые находятся в многопоточной очереди.
     */
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    /**
     * Достаёт все значения из хранилища(БД)
     * добавляет объекты городов с полем id,
     * название городов добавляется в слое сервисов.
     *
     * @return Lisl<Post>
     */
    public List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        Post post;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FROM)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    post = new Post(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getString("created"),
                            it.getBoolean("visible")
                    );
                    post.setCity(new City(it.getInt("city_id")));
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Создание вакансии.
     * Метод добавляет post в БД.
     * ps.getGeneratedKeys() - получаем все сгенерированные ключи
     * в цикле проходим по ResultSet и получаем ключ необходимой колонки.
     * @param post
     * @return Post
     */
    public Post addPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getCreated());
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Обновляет запись в БД.
     * Поля старой записи по id меняеться на
     * поля из переданого post.
     * @param post
     */
    public void update(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getCreated());
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getCity().getId());
            ps.setInt(6, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Находит запись в БД по id
     * и формирует на основе этой
     * записи новый обьект Post.
     * @param id
     * @return Post
     */

    public Post findPostById(int id) {
        Post post;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(SELECT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = new Post(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getString("created"),
                            it.getBoolean("visible")
                    );
                    post.setCity(new City(it.getInt("city_id")));
                    return post;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}