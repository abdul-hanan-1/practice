package com.example.practice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @PostMapping(value = "registeruser")
    public String registeruser(@Valid  User newUser) {
        List<User> users = userRepository.findAll();
        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            System.out.println("Registered user: " + newUser.toString());
            if (user.equals(newUser)) {
                System.out.println("User Already exists!");
                return "AlreadyExistsError";
            }
        }
        userRepository.save(newUser);
        return "SignupSuccess";
    }
    @PostMapping("/loginUser")
    public String loginUser(@Valid User user, RedirectAttributes redirAttrs) {
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.equals(user)) {
                user.setLoggedIn(true);
                userRepository.save(user);
                return "LoginSuccess";
            }
        }
        redirAttrs.addFlashAttribute("message", "Invalid username or password.");
        return "redirect:/login";
    }
    @PostMapping("/users/logout")
    public Status logUserOut(@Valid @RequestBody User user) {
        List<User> users = userRepository.findAll();
        for (User other : users) {
            if (other.equals(user)) {
                user.setLoggedIn(false);
                userRepository.save(user);
                return Status.SUCCESS;
            }
        }
        return Status.FAILURE;
    }
    @DeleteMapping("/users/all")
    public Status deleteUsers() {
        userRepository.deleteAll();
        return Status.SUCCESS;
    }
    @GetMapping("/register")
    public String register(final Model model){
        model.addAttribute("userData", new User());
        return "register";
    }
    @GetMapping("/login")
    public String login(final Model model){
        model.addAttribute("userData", new User());
        return "login";
    }
}