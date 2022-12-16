package ru.job4j.dreamjob.service;

import ch.qos.logback.classic.spi.CallerData;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;

@ThreadSafe
@Service

/**
 * Класс описывающий бизнесс логику работы приложения с моделью CANDIDATE.
 * Работа с хранилищем через сквозные вызовы классов персистенции.
 */
public class CandidateService {

    /**
     * Работа контроллеров с персистенцией идет через промежуточный слой
     * Service. CANDIDATE_STORE - константа для работы с CandidateStore дублируеться
     * чтобы не свзязать логику контроллеров и персистенции.
     */

    private final CandidateStore candidateStore;

    public CandidateService(CandidateStore candidateStore) {
        this.candidateStore = candidateStore;
    }

    /**
     * Предоставляет все значения хранилища.
     * @return Collection<Candidate>
     */

    public Collection<Candidate> findAll() {
        return candidateStore.findAll();
    }

    /**
     * Создать candidate.
     * Добавить во внутренее хранилище.
     * @param candidate
     */

    public void create(Candidate candidate) {
        candidateStore.create(candidate);
    }

    /**
     * Найти post по id
     * @param id
     * @return Candidate
     */
    public Candidate findById(int id) {
        return candidateStore.findById(id);
    }

    /**
     * Заменить запись во внутренем хранилище
     * на вновь переданую в аргументе.
     * @param candidate
     */
    public void update(Candidate candidate) {
        candidateStore.update(candidate);
    }
}
