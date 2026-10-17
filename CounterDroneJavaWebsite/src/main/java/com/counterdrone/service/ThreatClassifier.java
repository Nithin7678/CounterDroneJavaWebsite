package com.counterdrone.service;
import org.springframework.stereotype.Service;
import com.counterdrone.model.DroneDetection;

/** Transparent rule-based demo classifier. This is not a trained AI model. */
@Service
public class ThreatClassifier {
    public String classify(DroneDetection detection) {
        if (detection.isRestrictedArea() || detection.getDistanceMeters() <= 300) return "HIGH";
        if (detection.getDistanceMeters() <= 1000 || detection.getSpeedKmh() > 80) return "MEDIUM";
        return "LOW";
    }
    public String explanation(DroneDetection d) {
        if (d.isRestrictedArea()) return "High: reported inside a restricted area";
        if (d.getDistanceMeters() <= 300) return "High: within 300 metres";
        if (d.getDistanceMeters() <= 1000) return "Medium: within 1,000 metres";
        if (d.getSpeedKmh() > 80) return "Medium: speed above 80 km/h";
        return "Low: outside configured distance and speed thresholds";
    }
}
