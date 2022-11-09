package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ThreadSafe
@Repository
public class CandidateDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());

    private static final String SQL_SELECT_ALL = "SELECT * FROM candidate ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO candidate(name, description, created, city_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE candidate SET name = ?, description = ?, created = ?, city_id = ?, photo = ?"
                    + " WHERE id = ?";
    private static final String SQL_FIND_ID = "SELECT * FROM candidate WHERE id = ?";
    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(createNewCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error("CandidateDbStore. Ошибка в методе findAll - ", e);
        }
        return candidates;
    }

    public void addCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(SQL_INSERT,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, candidate.getCity().getId());
            ps.execute();
            try (ResultSet it = ps.getGeneratedKeys()) {
                if (it.next()) {
                    candidate.setId(it.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("CandidateDbStore. Ошибка в методе addCandidate - ", e);
        }
    }

    public void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.setInt(4, candidate.getCity().getId());
           // ps.setInt(5, candidate.getPhoto());
            ps.setInt(5, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("CandidateDbStore. Ошибка в методе updateCandidate - ", e);
        }
    }

    public Candidate findByIdCandidate(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(SQL_FIND_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = createNewCandidate(it);
                }
            }
        } catch (Exception e) {
            LOG.error("CandidateDbStore. Ошибка в методе findByIdCandidate - ", e);
        }
        return candidate;
    }

    private Candidate createNewCandidate(ResultSet it) throws SQLException {
        return new Candidate(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getTimestamp("created").toLocalDateTime(),
                new City(it.getInt("city_id"), null)
        );
    }
}
