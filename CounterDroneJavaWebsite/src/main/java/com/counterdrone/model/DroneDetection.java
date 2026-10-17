package com.counterdrone.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "drone_detections")
public class DroneDetection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=50) private String droneId;
    @Column(nullable=false, length=50) private String source;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="zone_id", nullable=false) private SurveillanceZone zone;
    @Column(nullable=false) private double distanceMeters;
    @Column(nullable=false) private double speedKmh;
    @Column(nullable=false) private boolean restrictedArea;
    @Column(nullable=false, length=20) private String threatLevel;
    @Column(nullable=false, length=20) private String alertStatus = "OPEN";
    @Column(nullable=false) private LocalDateTime detectedAt;
    public DroneDetection() {}
    @PrePersist void beforeSave(){ if(detectedAt==null) detectedAt=LocalDateTime.now(); if(alertStatus==null) alertStatus="OPEN"; }
    public String getFormattedTime(){return detectedAt==null?"—":detectedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));}
    public Long getId(){return id;} public String getDroneId(){return droneId;} public void setDroneId(String v){droneId=v;}
    public String getSource(){return source;} public void setSource(String v){source=v;} public SurveillanceZone getZone(){return zone;} public void setZone(SurveillanceZone v){zone=v;}
    public double getDistanceMeters(){return distanceMeters;} public void setDistanceMeters(double v){distanceMeters=v;}
    public double getSpeedKmh(){return speedKmh;} public void setSpeedKmh(double v){speedKmh=v;}
    public boolean isRestrictedArea(){return restrictedArea;} public void setRestrictedArea(boolean v){restrictedArea=v;}
    public String getThreatLevel(){return threatLevel;} public void setThreatLevel(String v){threatLevel=v;}
    public String getAlertStatus(){return alertStatus;} public void setAlertStatus(String v){alertStatus=v;}
    public LocalDateTime getDetectedAt(){return detectedAt;} public void setDetectedAt(LocalDateTime v){detectedAt=v;}
}
