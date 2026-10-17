package com.counterdrone.model;

import jakarta.persistence.*;

@Entity
@Table(name = "surveillance_zones")
public class SurveillanceZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String securityLevel;
    private boolean active = true;

    public SurveillanceZone() {
    }

    public SurveillanceZone(String name, String securityLevel, boolean active) {
        this.name = name;
        this.securityLevel = securityLevel;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(String securityLevel) { this.securityLevel = securityLevel; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
