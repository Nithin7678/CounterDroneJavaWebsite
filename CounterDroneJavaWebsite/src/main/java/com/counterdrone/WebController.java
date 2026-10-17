package com.counterdrone;

import com.counterdrone.repository.DroneDetectionRepository;
import com.counterdrone.repository.SurveillanceZoneRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final DroneDetectionRepository detections;
    private final SurveillanceZoneRepository zones;
    public WebController(DroneDetectionRepository detections, SurveillanceZoneRepository zones) { this.detections=detections; this.zones=zones; }

    @GetMapping("/") public String home(HttpSession session) { return session.getAttribute("username") == null ? "redirect:/login" : "redirect:/dashboard"; }
    @GetMapping("/login") public String loginPage() { return "login"; }
    @PostMapping("/login") public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        if (("admin".equals(username) && "Drone@123".equals(password)) || ("operator".equals(username) && "Secure@123".equals(password))) {
            session.setAttribute("username", username); return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid username or password."); return "login";
    }
    @GetMapping("/dashboard") public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("username") == null) return "redirect:/login";
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("detections", detections.findAllByOrderByDetectedAtDesc().stream().limit(5).toList());
        model.addAttribute("detectionCount", detections.count());
        model.addAttribute("openAlerts", detections.countByAlertStatusIgnoreCase("OPEN"));
        model.addAttribute("highCount", detections.countByThreatLevelIgnoreCase("HIGH"));
        model.addAttribute("zones", zones.findAllByOrderByNameAsc().stream().limit(5).toList());
        model.addAttribute("zoneCount", zones.count());
        return "dashboard";
    }
    @PostMapping("/logout") public String logout(HttpSession session) { session.invalidate(); return "redirect:/login?logout"; }
}
