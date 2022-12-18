package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
@Repository
/**
 * Слой персистенции.
 * Модель данных.
 * Класс хранилище кандидатов Candidate в памяти.
 * Синглтон.
 */

public class CandidateStore {

    private static final AtomicInteger CANDIDATE_ID =  new AtomicInteger(4);

    private AtomicReference<String> date = new AtomicReference<>(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        candidates.put(1, new Candidate(1, "Carey Mahoney", "description of Mahoney",
                date.get(), true, new byte[]{0}));
        candidates.put(2, new Candidate(2, "Moses Hightower", "description of Hightower",
                date.get(), true, new byte[]{0}));
        candidates.put(3, new Candidate(3, "Eugene Tackleberry", "description of Tackleberry",
                date.get(), true, new byte[]{0}));
    }

    /**
     * Геттер  всех значений кандидатов
     * @return Collection<Candidate>
     */

    public List<Candidate> findAll() {
        return (List<Candidate>) candidates.values();
    }

    /**
     * Метод добавляет candidate во внутреннее
     * хранилище candidate(map) о ключую.
     * @param candidate
     */
    public void create(Candidate candidate) {
        candidate.setId(CANDIDATE_ID.getAndIncrement());
        date = new AtomicReference<>(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        candidate.setCreated(date.get());
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    /**
     * Обновляет запись candidateStore.
     * старая запись по id меняеться на
     * переданый post.
     * @param candidate
     */
    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidate);
    }
}
