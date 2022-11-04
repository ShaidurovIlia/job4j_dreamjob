package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.persistence.CandidateStore;

import java.util.Collection;
@ThreadSafe
@Service
public class CandidateService {
    private final CandidateStore storeCandidate;

    public CandidateService(CandidateStore storeCandidate) {
        this.storeCandidate = storeCandidate;
    }

    public Collection<Candidate> findAll() {
        return storeCandidate.findAll();
    }

    public void addCandidate(Candidate candidate) {
        storeCandidate.addCandidate(candidate);
    }

    public Candidate findByIdCandidate(int id) {
        return storeCandidate.findByIdCandidate(id);
    }

    public void updateCandidate(Candidate candidate) {
        storeCandidate.updateCandidate(candidate);
    }
}
