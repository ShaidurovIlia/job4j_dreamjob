package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ThreadSafe
@Repository
public class UserDBStore {

    private static final String INSERT_USER = "INSERT INTO users(email, name, password)"
            + " VALUES (?, ?, ?)";

    private static final String SELECT_EMAIL_PSW = "SELECT * FROM users WHERE email = ?"
            + " AND password = ?";
    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    /**
     * Добавляем user в БД.
     * На поле email ноложено ограничение UNIQUE,
     * при добавлении дубликата email в бд, возникнет
     * исключение и метод вернет пустой Optional.
     *
     * @param user
     * @return Optional<User>
     */
    public Optional<User> add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_USER,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.executeQuery()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    /**
     * Находит user в БД по email и password.
     * @param email
     * @param password
     * @return
     */
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        User user;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_EMAIL_PSW)
        ) {
            ps.setString(1, "email");
            ps.setString(2, "password");
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    user = new User(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("email"),
                            it.getString("password")
                    );
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
