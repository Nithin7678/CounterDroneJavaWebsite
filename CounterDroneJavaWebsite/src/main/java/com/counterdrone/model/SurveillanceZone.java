package com.counterdrone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "surveillance_zones")
public class SurveillanceZone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 80)
    @Column(nullable = false, unique = true, length = 80)
    private String name;
    @NotBlank @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String securityLevel = "Medium";
    @Column(nullable = false)
    private boolean active = true;
    public SurveillanceZone() {}
    public SurveillanceZone(String name, String securityLevel, boolean active) { this.name=name; this.securityLevel=securityLevel; this.active=active; }
    public Long getId(){return id;} public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getSecurityLevel(){return securityLevel;} public void setSecurityLevel(String securityLevel){this.securityLevel=securityLevel;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
}
