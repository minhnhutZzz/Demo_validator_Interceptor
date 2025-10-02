package vn.iotstar.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

import vn.iotstar.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "user/home"; // Tương ứng với templates/user/home.html
        }
        return "redirect:/login";
    }
}