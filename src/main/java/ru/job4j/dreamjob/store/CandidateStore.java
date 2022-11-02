package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore CST = new CandidateStore();
    private final Map<Integer, Candidate> candidate = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidate.put(1, new Candidate(1, "Artur", "Java EE", LocalDateTime.now()));
        candidate.put(2, new Candidate(2, "Olga", "Spring", LocalDateTime.now()));
        candidate.put(3, new Candidate(3, "Ivan", "Hibernate", LocalDateTime.now()));
    }

    public static CandidateStore instOf() {
        return CST;
    }

    public Collection<Candidate> findAll() {
        return candidate.values();
    }
}
