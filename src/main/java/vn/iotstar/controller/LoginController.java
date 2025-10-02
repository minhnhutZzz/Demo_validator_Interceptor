package vn.iotstar.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String passwd, HttpSession session, Model model) {
        User user = userService.findByEmailAndPasswd(email, passwd);
        if (user != null) {
            session.setAttribute("user", user);
            return user.isAdmin() ? "redirect:/admin/categories" : "redirect:/user/home";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }
}
