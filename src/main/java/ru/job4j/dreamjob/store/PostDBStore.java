package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class PostDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());
    private static final String SQL_SELECT_ALL = "SELECT * FROM post ORDER BY id";

    private static final String SQL_INSERT =
            "INSERT INTO post(name, description, created, city_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE post SET name = ?, description = ?, created = ?, city_id = ? WHERE id = ?";
    private static final String SQL_FIND_ID = "SELECT * FROM post WHERE id = ?";
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createNewPost(it));
                }
            }
        } catch (Exception e) {
            LOG.error("PostDBStore. Ошибка в методе findAll - ", e);
        }
        return posts;
    }

    public void addPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SQL_INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.setInt(4, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("PostDBStore. Ошибка в методе addPost - ", e);
        }
    }

    public void updatePost(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                SQL_UPDATE)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, post.getCity().getId());
            ps.setInt(5, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("PostDBStore. Ошибка в методе updatePost - ", e);
        }
    }

    public Post findByIdPost(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     SQL_FIND_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createNewPost(it);
                }
            }
        } catch (Exception e) {
            LOG.error("PostDBStore. Ошибка в методе findByIdPost - ", e);
        }
        return null;
    }

    public Post createNewPost(ResultSet it) throws SQLException {
        return new Post(it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime(),
                new City(it.getInt("city_id"), null)
        );
    }
}