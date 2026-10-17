package com.counterdrone;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @GetMapping("/")
    public String home(HttpSession session) {
        return session.getAttribute("username") == null ? "redirect:/login" : "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        // Demo credentials only. Replace with database-backed authentication before real use.
        if (("admin".equals(username) && "Drone@123".equals(password)) ||
            ("operator".equals(username) && "Secure@123".equals(password))) {
            session.setAttribute("username", username);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("username") == null) return "redirect:/login";
        model.addAttribute("username", session.getAttribute("username"));
        return "dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
