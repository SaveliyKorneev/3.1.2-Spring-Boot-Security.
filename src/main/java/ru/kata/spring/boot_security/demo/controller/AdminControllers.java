package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminControllers {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminControllers(UserService userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", userService.index());
        return "admin/index";
    }


    @GetMapping("/show")
    public String show(@RequestParam("id") Long id, Model model) {
        User user = userService.show(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "redirect:/admin";
        }
        model.addAttribute("user", userService.show(id));
        return "admin/show";
    }


    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user,Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/new";
    }


    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @RequestParam Set<Long> roleIds, Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/new";
        }
        userService.save(user, roleIds);
        return "redirect:/admin";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userService.show(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "redirect:/admin";
        }
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("user", user);
        return "admin/edit";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam Set<Long> roleIds) {
        if (bindingResult.hasErrors())
            return "admin/edit";

        userService.update(id, user, roleIds);
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
