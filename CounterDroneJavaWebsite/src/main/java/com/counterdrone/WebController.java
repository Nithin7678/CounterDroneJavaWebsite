
package com.counterdrone;

import com.counterdrone.repository.DroneDetectionRepository;
import com.counterdrone.repository.SurveillanceZoneRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class WebController {

    private final DroneDetectionRepository detections;
    private final SurveillanceZoneRepository zones;

    public WebController(
            DroneDetectionRepository detections,
            SurveillanceZoneRepository zones) {
        this.detections = detections;
        this.zones = zones;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        return session.getAttribute("username") == null
                ? "redirect:/login"
                : "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (("admin".equals(username) && "Drone@123".equals(password))
                || ("operator".equals(username)
                && "Secure@123".equals(password))) {

            session.setAttribute("username", username);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("username"));

        // Read actual database records first.
        var actualDetections =
                detections.findAllByOrderByDetectedAtDesc();

        if (actualDetections.isEmpty()) {

            // DEMO DATA ONLY: these are not real drone detections.
            List<DemoDetection> demoDetections = List.of(
                    new DemoDetection(
                            "SIM-DR01", "Simulation", "North Gate",
                            "LOW", "OPEN", LocalDateTime.now().minusMinutes(1)),
                    new DemoDetection(
                            "SIM-DR02", "Simulation", "East Zone",
                            "MEDIUM", "OPEN", LocalDateTime.now().minusMinutes(2)),
                    new DemoDetection(
                            "SIM-DR03", "Simulation", "South Gate",
                            "HIGH", "OPEN", LocalDateTime.now().minusMinutes(3)),
                    new DemoDetection(
                            "SIM-DR04", "Simulation", "West Zone",
                            "LOW", "CLOSED", LocalDateTime.now().minusMinutes(4)),
                    new DemoDetection(
                            "SIM-DR05", "Simulation", "North Gate",
                            "MEDIUM", "OPEN", LocalDateTime.now().minusMinutes(5))
            );

            model.addAttribute("detections", demoDetections);
            model.addAttribute("detectionCount", demoDetections.size());
            model.addAttribute("openAlerts", 4);
            model.addAttribute("highCount", 1);

        } else {

            model.addAttribute(
                    "detections",
                    actualDetections.stream().limit(5).toList());

            model.addAttribute("detectionCount", detections.count());
            model.addAttribute(
                    "openAlerts",
                    detections.countByAlertStatusIgnoreCase("OPEN"));
            model.addAttribute(
                    "highCount",
                    detections.countByThreatLevelIgnoreCase("HIGH"));
        }

        model.addAttribute(
                "zones",
                zones.findAllByOrderByNameAsc().stream().limit(5).toList());

        model.addAttribute("zoneCount", zones.count());

        return "dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // Helper classes for displaying sample data in Thymeleaf.
    public static class DemoDetection {
        private final String droneId;
        private final String source;
        private final DemoZone zone;
        private final String threatLevel;
        private final String alertStatus;
        private final LocalDateTime detectedAt;

        public DemoDetection(
                String droneId,
                String source,
                String zoneName,
                String threatLevel,
                String alertStatus,
                LocalDateTime detectedAt) {
            this.droneId = droneId;
            this.source = source;
            this.zone = new DemoZone(zoneName);
            this.threatLevel = threatLevel;
            this.alertStatus = alertStatus;
            this.detectedAt = detectedAt;
        }

        public String getDroneId() {
            return droneId;
        }

        public String getSource() {
            return source;
        }

        public DemoZone getZone() {
            return zone;
        }

        public String getThreatLevel() {
            return threatLevel;
        }

        public String getAlertStatus() {
            return alertStatus;
        }

        public String getFormattedTime() {
            return detectedAt.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }
    }

    public static class DemoZone {
        private final String name;

        public DemoZone(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
