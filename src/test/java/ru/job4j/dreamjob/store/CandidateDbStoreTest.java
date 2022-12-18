package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDbStoreTest {

    @Test
    public void whetCreateCandidateThenFindById() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Artur", "Developer",
                "11.11.11", true, new byte[]{0});
        store.addCandidate(candidate);
        Candidate candidateInId = store.findCandidateById(candidate.getId());
        assertThat(candidateInId.getName(), is(candidate.getName()));
    }

    @Test
    public void whenCreatePostThenFindAll() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(1, "Artur", "Developer",
                "11.11.11", true, new byte[]{0});
        Candidate candidateTwo = new Candidate(2, "Anna", "UA/UX",
                "01.06.12", true, new byte[]{0});
        store.addCandidate(candidate);
        store.addCandidate(candidateTwo);
        List<Candidate> expected = List.of(candidate, candidateTwo);
        List<Candidate> allCandidate = store.findAllCandidate();
        List<Candidate> rsl = allCandidate.subList(allCandidate.size() - 2, allCandidate.size());
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenUpdateCandidateThenFindById() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(1, "Artur", "Developer",
                "11.11.11", true, new byte[]{0});
        store.addCandidate(candidate);
        Candidate candidateTwo = new Candidate(2, "Anna", "UA/UX",
                "01.06.12", true, new byte[]{0});
        store.update(candidateTwo);
        Candidate candidateInDB = store.findCandidateById(candidate.getId());
        assertThat(candidateInDB, is(candidateTwo));
    }
}