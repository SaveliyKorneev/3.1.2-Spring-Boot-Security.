package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminControllers {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminControllers(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", userServiceImpl.index());
        return "admin/index";
    }


    @GetMapping("/show")
    public String show(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.show(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "redirect:/admin";
        }
        model.addAttribute("user", userServiceImpl.show(id));
        return "admin/show";
    }


    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }


    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/new";
        }

        userServiceImpl.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.show(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        return "admin/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id) {
        if (bindingResult.hasErrors())
            return "admin/edit";

        userServiceImpl.update(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }


}
