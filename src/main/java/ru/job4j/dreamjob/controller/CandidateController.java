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
@ThreadSafe
@Controller
public class CandidateController {
    private final CandidateService candidateService;
    private final CityService cityService;

    public CandidateController(CandidateService candidate, CityService cityService) {
        this.candidateService = candidate;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        model.addAttribute("candidates",
                new Candidate(0, "Заполните поле", "Заполните поле", LocalDateTime.now(),
                        new City(0, "Выберете город из списка")));
        model.addAttribute("cities", cityService.getAllCities());
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidate.setCreated(LocalDateTime.now());
        candidate.setPhoto(file.getBytes());
        candidateService.addCandidate(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidates/{postId}")
    public String formUpdateCandidate(Model model, @PathVariable("postId") int id) {
        model.addAttribute("candidateUPD", candidateService.findByIdCandidate(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidates";
    }

    @PostMapping("/updateCandidates")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidate.setPhoto(file.getBytes());
        candidateService.updateCandidate(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findByIdCandidate(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }
}