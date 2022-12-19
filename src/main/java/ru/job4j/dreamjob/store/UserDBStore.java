package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@ThreadSafe
@Repository
public class UserDBStore {

    private static final String INSERT_USER = "INSERT INTO users(email, name, password)"
            + " VALUES (?, ?, ?)";
    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    /**
     * Добавляем user в БД.
     * На поле email ноложено ограничение UNIQUE,
     * при добавлении дубликата email в бд, возникнет
     * исключение и метод вернет пустой Optional.
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
}
