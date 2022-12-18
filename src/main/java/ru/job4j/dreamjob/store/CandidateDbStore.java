package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class CandidateDbStore {

    private static final String FROM = "SELECT * FROM candidate";
    private static final String INSERT = "INSERT INTO candidate"
            + "(name, description, created, visible, city_id, photo) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate SET name = ?, description = ?,"
            + " created = ?, visible = ?, city_id = ?, photo = ? WHERE id = ?";
    private static final String SELECT = "SELECT * FROM candidate WHERE id = ?";
    /**
     * Внутри объекта создаются коннекты к базе данных,
     * которые находятся в многопоточной очереди.
     */
    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;

    }

    /**
     * Достает все значения из хранилища БД
     * добавляет объекты городов с полем id,
     * название городов добавляется в слое сервисов.
     * @return List<Candidate>
     */
    public List<Candidate> findAllCandidate() {
        List<Candidate> candidates = new ArrayList<>();
        Candidate candidate;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FROM)
                ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidate = new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getString("created"),
                            it.getBoolean("visible"),
                            it.getBytes("photo")
                    );
                    candidate.setCity(new City(it.getInt("city_id")));
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    /**
     * Создание кандидата.
     * Метод добавляет candidate в БД.
     * ps.getGeneratedKeys() - получаем все сгенерированные ключи
     * в цикле проходим по ResultSet и получаем ключ необходимой колонки.
     * @param candidate
     * @return Candidate
     */
    public Candidate addCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setString(3, candidate.getCreated());
            ps.setBoolean(4, candidate.isVisible());
            ps.setInt(5, candidate.getCity().getId());
            ps.setBytes(6, candidate.getPhoto());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    /**
     * Обновляет запись в БД.
     * Поля старой записи по id меняеться на
     * поля из переданого candidate.
     * @param candidate
     */
    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setString(3, candidate.getCreated());
            ps.setBoolean(4, candidate.isVisible());
            ps.setInt(5, candidate.getCity().getId());
            ps.setBytes(6, candidate.getPhoto());
            ps.setInt(7, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Находит запись в БД по id
     * и формирует на основе этой
     * записи новый обьект Candidate.
     * @param id
     * @return Candidate
     */

    public Candidate findCandidateById(int id) {
        Candidate candidate;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                candidate = new Candidate(
                        it.getInt("id"),
                        it.getString("name"),
                        it.getString("description"),
                        it.getString("created"),
                        it.getBoolean("visible"),
                        it.getBytes("photo")
                );
                candidate.setCity(new City(it.getInt("city_id")));
                return candidate;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
