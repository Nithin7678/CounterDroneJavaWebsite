
package com.counterdrone;

import com.counterdrone.repository.DroneDetectionRepository;
import com.counterdrone.repository.SurveillanceZoneRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        // Use saved database records when available.
        var actualDetections =
                detections.findAllByOrderByDetectedAtDesc();

        if (actualDetections.isEmpty()) {

            // DEMO ONLY: these are simulated, not real drone detections.
            List<DemoDetection> demoDetections =
                    generateDemoDetections();

            model.addAttribute("detections", demoDetections);
            model.addAttribute("detectionCount", demoDetections.size());

            model.addAttribute(
                    "openAlerts",
                    (int) demoDetections.stream()
                            .filter(d -> "OPEN".equals(d.getAlertStatus()))
                            .count());

            model.addAttribute(
                    "highCount",
                    (int) demoDetections.stream()
                            .filter(d -> "HIGH".equals(d.getThreatLevel()))
                            .count());

            model.addAttribute("simulationMode", true);

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

            model.addAttribute("simulationMode", false);
        }

        model.addAttribute(
                "zones",
                zones.findAllByOrderByNameAsc().stream().limit(5).toList());

        model.addAttribute("zoneCount", zones.count());

        return "dashboard";
    }

    // Generate five changing sample records whenever the dashboard loads.
    private List<DemoDetection> generateDemoDetections() {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        String[] zoneNames = {
                "North Gate",
                "East Zone",
                "South Gate",
                "West Zone"
        };

        String[] threatLevels = {"LOW", "MEDIUM", "HIGH"};

        List<DemoDetection> demo = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            String droneId =
                    "SIM-DR" + random.nextInt(1000, 10000);

            String zone =
                    zoneNames[random.nextInt(zoneNames.length)];

            String threat =
                    threatLevels[random.nextInt(threatLevels.length)];

            String status =
                    random.nextBoolean() ? "OPEN" : "CLOSED";

            demo.add(new DemoDetection(
                    droneId,
                    "Simulation",
                    zone,
                    threat,
                    status,
                    LocalDateTime.now()
                            .minusSeconds(random.nextInt(300))
            ));
        }

        return demo;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // Helper class for simulated records displayed by Thymeleaf.
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
