package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

class CandidateDBStoreTest {

    private static CandidateDBStore candidateStore;

    @BeforeEach
    void before() throws SQLException {
        new Main().loadPool().getConnection().prepareStatement("delete from candidate").execute();
        candidateStore = new CandidateDBStore(new Main().loadPool());
    }

    @Test
    void findAll() {
        Candidate candidate1 = new Candidate(1, "a", "a", LocalDateTime.now(),
                new City(1, "a"), new byte[1]);
        Candidate candidate2 = new Candidate(2, "b", "b", LocalDateTime.now(),
                new City(2, "b"), new byte[1]);
        candidateStore.addCandidate(candidate1);
        candidateStore.addCandidate(candidate2);
        List<Candidate> all = candidateStore.findAll();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    void addPost() {
        Candidate candidate1 = new Candidate(1, "a", "a", LocalDateTime.now(),
                new City(1, "a"), new byte[1]);
        candidateStore.addCandidate(candidate1);
        Assertions.assertEquals(1, candidateStore.findAll().size());
        Assertions.assertTrue(candidateStore.findAll().contains(candidate1));
    }
}