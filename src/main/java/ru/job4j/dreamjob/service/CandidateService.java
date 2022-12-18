package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDbStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
@Service

/**
 * Класс описывающий бизнесс логику работы приложения с моделью CANDIDATE.
 * Работа с хранилищем через сквозные вызовы классов персистенции.
 */
public class CandidateService {

    /**
     * Дата создания с CAS операциями.
     */
    private AtomicReference<String> date = new AtomicReference<>(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    /**
     * Работа с БД через слой персистенции.
     */
   private final CandidateDbStore candidateDbStore;

    /**
     * Сервис городов для установки в обьекты post
     */
   private final CityService cityService;

    public CandidateService(CandidateDbStore candidateDbStore, CityService cityService) {
        this.candidateDbStore = candidateDbStore;
        this.cityService = cityService;
    }

    /**
     * Предоставляет все значения хранилища.
     * @return Collection<Candidate>
     */

    public List<Candidate> findAllCandidates() {
        List<Candidate> candidates = candidateDbStore.findAllCandidate();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId()))
        );
        return candidates;
    }

    /**
     * Создать candidate.
     * Добавить во внутренее хранилище.
     * Установить дату при создании.
     * @param candidate
     */

    public void create(Candidate candidate) {
        candidate.setCreated(date.get());
        candidateDbStore.addCandidate(candidate);
    }

    /**
     * Найти candidate по id
     * @param id
     * @return Candidate
     */
    public Candidate findCandidateById(int id) {
        return candidateDbStore.findCandidateById(id);
    }

    /**
     * Заменить запись во внутренем хранилище
     * на вновь переданую в аргументе.
     * @param candidate
     */
    public void update(Candidate candidate) {
       candidateDbStore.update(candidate);
    }
}
