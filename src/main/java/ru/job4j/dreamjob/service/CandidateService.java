package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDBStore;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class CandidateService {
    private final CandidateDBStore storeCandidate;
    private final CityService cityService;

    public CandidateService(CandidateDBStore store, CityService city) {
        this.storeCandidate = store;
        this.cityService = city;
    }

    public Collection<Candidate> findAll() {
        List<Candidate> candidates = storeCandidate.findAll();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId())
                )
        );
        return candidates;
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
