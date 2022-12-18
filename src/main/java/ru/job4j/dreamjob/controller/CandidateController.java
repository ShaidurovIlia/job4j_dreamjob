package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ThreadSafe
@Controller
/**
 * Контролер (Блок управления) обьектами Candidate
 */
public class CandidateController {

    /**
     * Работа с CandidateStore через промежуточный слой CandidateService
     */
   private final CandidateService candidateService;

   private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    /**
     * Используется Thymeleaf для поиска объектов,
     * которые нужны отобразить на виде (View).
     * @return String
     */
    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candidateService.findAllCandidates());
        return "candidates";
    }

    /**
     * Обрабатывает переход на addCandidate.html
     * @param model
     * @return
     * @return String
     */
    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        model.addAttribute("candidate", new Candidate(0, "Заполните поле",
                "", date, false, new byte[]{0}));
        model.addAttribute("cities", cityService.getAllCities());
        return "addCandidate";
    }

    /**
     * Обрабатывает добавление данных в candidate
     * и их сохранение в store.
     * Города в объекте candidate не имеют имени,
     * поэтому достаем его из словаря.
     * @param candidate
     * @return String
     */
    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file")MultipartFile file) throws IOException {
        int id = candidate.getCity().getId();
        City city = cityService.findById(id);
        candidate.setCity(city);
        candidate.setPhoto(file.getBytes());
        candidateService.create(candidate);
        return "redirect:/candidates";
    }

    /**
     * 1.Метод загрузки файла (фото) в форму candidates.html
     * 2.ResponseEntity - класс для формирования ответа:
     * - указываем content-type (тип ответа)
     * для всех файлов тип ответ - "application/octet-stream".
     * - браузер загружает тег img он отправляет новый
     * запрос на сервер по адресу указанному в атрибуте src
     * (адрес src находится candidates.html)
     * 3.Сервер преобразует массив байт в строку в кодировке BASE64.
     * В свою очередь браузер преобразует ее в изображение.
     * @param candidateId
     * @return
     */
    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findCandidateById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }

    /**
     * Обрабатывает переход на updateCandidate.html
     * @param model
     * @param id
     * @return String
     */

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute("candidate", candidateService.findCandidateById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidate";
    }

    /**
     * Сохраняет данные в candidate
     * после редактирования.
     * Города в обьекте candidate не имеют имени,
     * поэтому достаем его из славоря.
     * @param candidate
     * @return
     */
    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        int id = candidate.getCity().getId();
        City city = cityService.findById(id);
        candidate.setCity(city);
        candidate.setPhoto(file.getBytes());
        candidateService.update(candidate);
        return "redirect:/candidates";
    }
}

