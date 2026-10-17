
package com.counterdrone.service;

import com.counterdrone.model.DroneDetection;
import com.counterdrone.model.SurveillanceZone;
import com.counterdrone.repository.DroneDetectionRepository;
import com.counterdrone.repository.SurveillanceZoneRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DroneSimulationService {

    private final DroneDetectionRepository detectionRepository;
    private final SurveillanceZoneRepository zoneRepository;

    public DroneSimulationService(
            DroneDetectionRepository detectionRepository,
            SurveillanceZoneRepository zoneRepository) {
        this.detectionRepository = detectionRepository;
        this.zoneRepository = zoneRepository;
    }

    // Generate one simulated detection every 15 seconds.
    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    @Transactional
    public void generateDetection() {

        List<SurveillanceZone> activeZones =
                zoneRepository.findAllByOrderByNameAsc()
                        .stream()
                        .filter(SurveillanceZone::isActive)
                        .toList();

        // Wait until at least one active zone exists.
        if (activeZones.isEmpty()) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();

        SurveillanceZone zone =
                activeZones.get(random.nextInt(activeZones.size()));

        double distance = random.nextDouble(20.0, 500.0);
        double speed = random.nextDouble(10.0, 120.0);
        boolean restricted = random.nextInt(100) < 30;

        String threatLevel;

        if (restricted && distance < 100) {
            threatLevel = "HIGH";
        } else if (restricted || distance < 200) {
            threatLevel = "MEDIUM";
        } else {
            threatLevel = "LOW";
        }

        DroneDetection detection = new DroneDetection();

        detection.setDroneId(
                "SIM-" + random.nextInt(1000, 10000));
        detection.setSource("SIMULATION");
        detection.setZone(zone);
        detection.setDistanceMeters(distance);
        detection.setSpeedKmh(speed);
        detection.setRestrictedArea(restricted);
        detection.setThreatLevel(threatLevel);
        detection.setAlertStatus("OPEN");
        detection.setDetectedAt(LocalDateTime.now());

        detectionRepository.save(detection);

        System.out.println(
                "SIMULATED detection created: "
                + detection.getDroneId()
                + " | Threat: " + threatLevel
                + " | Zone: " + zone.getName());
    }
}
