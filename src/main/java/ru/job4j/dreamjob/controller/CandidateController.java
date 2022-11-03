package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.time.LocalDateTime;

@Controller
public class CandidateController {

    private final CandidateStore  candControl = CandidateStore.instOf();

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candControl.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        model.addAttribute("candidates", new Candidate(0, "Заполните поле",
                "Заполните поле", LocalDateTime.now()));
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate) {
        candControl.addCandidate(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{postId}")
    public String formUpdateCandidate(Model model, @PathVariable("postId") int id) {
        model.addAttribute("candidateUPD", candControl.findByIdCandidate(id));
        return "updateCandidates";
    }

    @PostMapping("/updateCandidates")
    public String updateCandidate(@ModelAttribute Candidate candidate) {
        candControl.updateCandidate(candidate);
        return "redirect:/candidates";
    }
}
