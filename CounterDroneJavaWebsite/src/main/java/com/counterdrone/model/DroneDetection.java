package com.counterdrone.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drone_detections")
public class DroneDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String droneId;

    private String source;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    private SurveillanceZone zone;

    private double distanceMeters;
    private double speedKmh;
    private boolean restrictedArea;
    private String threatLevel;
    private String alertStatus;

    private LocalDateTime detectedAt;

    public DroneDetection() {
    }

    @PrePersist
    public void setDefaultDetectedAt() {
        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDroneId() { return droneId; }
    public void setDroneId(String droneId) { this.droneId = droneId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public SurveillanceZone getZone() { return zone; }
    public void setZone(SurveillanceZone zone) { this.zone = zone; }

    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

    public double getSpeedKmh() { return speedKmh; }
    public void setSpeedKmh(double speedKmh) { this.speedKmh = speedKmh; }

    public boolean isRestrictedArea() { return restrictedArea; }
    public void setRestrictedArea(boolean restrictedArea) { this.restrictedArea = restrictedArea; }

    public String getThreatLevel() { return threatLevel; }
    public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }

    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }

    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }

    public String getFormattedTime() {
        return detectedAt == null ? "" :
                detectedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
