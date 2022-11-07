package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

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
                        new City(0, "Выберите город из списка")));
        model.addAttribute("cities", cityService.getAllCities());
        return "AddCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate) {
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
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
    public String updateCandidate(@ModelAttribute Candidate candidate) {
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidateService.updateCandidate(candidate);
        return "redirect:/candidates";
    }
}