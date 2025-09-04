package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserControllers {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;

    @Autowired
    public UserControllers(UserValidator userValidator, RegistrationService registrationService, UserRepository userRepository) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "user/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult
            bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "user/registration";
        registrationService.register(user);
        return "redirect:/user/login";
    }
    @GetMapping("/page")
    public String pageUser(Model model, Principal principal) {
        model.addAttribute("user", userRepository.findByUsername(principal.getName()));
        return "user/page";
    }
}
