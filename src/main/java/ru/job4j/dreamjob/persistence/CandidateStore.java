package ru.job4j.dreamjob.persistence;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CandidateStore {
    private final AtomicInteger idS = new AtomicInteger(1);

    private final Map<Integer, Candidate> candidateStore = new ConcurrentHashMap<>();

    public Collection<Candidate> findAll() {
        return candidateStore.values();
    }

    public void addCandidate(Candidate candidate) {
        candidate.setId(idS.getAndIncrement());
        candidate.setCreated(LocalDateTime.now());
        candidateStore.put(candidate.getId(), candidate);
    }

    public Candidate findByIdCandidate(int id) {
        return candidateStore.get(id);
    }

    public void updateCandidate(Candidate candidate) {
        candidateStore.replace(candidate.getId(), candidate);
    }
}
