package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

@ThreadSafe
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * загружает страницу addUser.html.
     * @param model
     * @return
     */
    @GetMapping("/formAddUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    /**
     * Обрабатывает добавление данных
     * из полеей ввода в обьект user и
     * и последующеее сохранение в UserDB.
     * @param model
     * @param user
     * @return String
     */

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "пользователь с такой почтой уже сущетвует");
            return "redirect:/fail";
        }
        return "redirect:/success";
    }

    /**
     * Загружает страницу login.html
     * @param model
     * @param fail
     * @return String
     */
    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(
            name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    /**
     * Обрабатывает входящие параметры на странице
     * login.html
     * Проверка на пустой Optional
     * @param user
     * @return String
     */
    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        Optional<User> userDb = userService.findUserByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        return "redirect:/index";
    }
}
