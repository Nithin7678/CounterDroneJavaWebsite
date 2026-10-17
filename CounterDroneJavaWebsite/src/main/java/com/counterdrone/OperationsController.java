package com.counterdrone;

import com.counterdrone.model.DroneDetection;
import com.counterdrone.model.SurveillanceZone;
import com.counterdrone.repository.DroneDetectionRepository;
import com.counterdrone.repository.SurveillanceZoneRepository;
import com.counterdrone.service.ThreatClassifier;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OperationsController {
    private final DroneDetectionRepository detections;
    private final SurveillanceZoneRepository zones;
    private final ThreatClassifier classifier;
    public OperationsController(DroneDetectionRepository detections, SurveillanceZoneRepository zones, ThreatClassifier classifier) { this.detections=detections; this.zones=zones; this.classifier=classifier; }
    private boolean loggedIn(HttpSession s){return s.getAttribute("username")!=null;}

    @GetMapping("/detections") public String detections(HttpSession s, Model m) { if(!loggedIn(s)) return "redirect:/login"; m.addAttribute("detections", detections.findAllByOrderByDetectedAtDesc()); return "detections"; }
    @GetMapping("/detections/new") public String newDetection(HttpSession s, Model m) { if(!loggedIn(s)) return "redirect:/login"; m.addAttribute("detection", new DroneDetection()); m.addAttribute("zones", zones.findAllByOrderByNameAsc().stream().filter(SurveillanceZone::isActive).toList()); return "detection-form"; }
    @PostMapping("/detections") public String saveDetection(HttpSession s, @RequestParam String droneId, @RequestParam String source, @RequestParam Long zoneId, @RequestParam double distanceMeters, @RequestParam double speedKmh, @RequestParam(defaultValue="false") boolean restrictedArea, RedirectAttributes flash) {
        if(!loggedIn(s)) return "redirect:/login";
        if(droneId.isBlank() || source.isBlank() || distanceMeters<0 || speedKmh<0){flash.addFlashAttribute("error","Enter valid values. Distance and speed cannot be negative.");return "redirect:/detections/new";}
        SurveillanceZone zone=zones.findById(zoneId).orElse(null); if(zone==null || !zone.isActive()){flash.addFlashAttribute("error","Choose an active surveillance zone.");return "redirect:/detections/new";}
        DroneDetection d=new DroneDetection(); d.setDroneId(droneId.trim()); d.setSource(source.trim()); d.setZone(zone); d.setDistanceMeters(distanceMeters); d.setSpeedKmh(speedKmh); d.setRestrictedArea(restrictedArea); d.setThreatLevel(classifier.classify(d)); d.setAlertStatus("OPEN"); detections.save(d);
        flash.addFlashAttribute("success","Detection saved. Rule-based classification: "+d.getThreatLevel()+"."); return "redirect:/detections";
    }
    @GetMapping("/alerts") public String alerts(HttpSession s, Model m) { if(!loggedIn(s)) return "redirect:/login"; m.addAttribute("alerts", detections.findByThreatLevelNotIgnoreCaseOrderByDetectedAtDesc("LOW")); return "alerts"; }
    @PostMapping("/alerts/{id}/review") public String reviewAlert(HttpSession s, @PathVariable Long id, RedirectAttributes flash) { if(!loggedIn(s)) return "redirect:/login"; detections.findById(id).ifPresent(d->{d.setAlertStatus("REVIEWED");detections.save(d);}); flash.addFlashAttribute("success","Alert marked as reviewed."); return "redirect:/alerts"; }
    @GetMapping("/zones") public String zones(HttpSession s, Model m) { if(!loggedIn(s)) return "redirect:/login"; m.addAttribute("zones", zones.findAllByOrderByNameAsc()); m.addAttribute("zone", new SurveillanceZone()); return "zones"; }
    @PostMapping("/zones") public String addZone(HttpSession s, @RequestParam String name, @RequestParam String securityLevel, RedirectAttributes flash) { if(!loggedIn(s)) return "redirect:/login"; String n=name.trim(); if(n.isBlank()){flash.addFlashAttribute("error","Zone name is required.");return "redirect:/zones";} if(zones.existsByNameIgnoreCase(n)){flash.addFlashAttribute("error","A zone with that name already exists.");return "redirect:/zones";} zones.save(new SurveillanceZone(n, securityLevel, true)); flash.addFlashAttribute("success","Surveillance zone added."); return "redirect:/zones"; }
    @PostMapping("/zones/{id}/toggle") public String toggleZone(HttpSession s, @PathVariable Long id, RedirectAttributes flash) { if(!loggedIn(s)) return "redirect:/login"; zones.findById(id).ifPresent(z->{z.setActive(!z.isActive());zones.save(z);}); flash.addFlashAttribute("success","Zone status updated."); return "redirect:/zones"; }
    @GetMapping("/reports") public String reports(HttpSession s, Model m) { if(!loggedIn(s)) return "redirect:/login"; var all=detections.findAllByOrderByDetectedAtDesc(); m.addAttribute("total", all.size()); m.addAttribute("high", detections.countByThreatLevelIgnoreCase("HIGH")); m.addAttribute("medium", detections.countByThreatLevelIgnoreCase("MEDIUM")); m.addAttribute("low", detections.countByThreatLevelIgnoreCase("LOW")); m.addAttribute("open", detections.countByAlertStatusIgnoreCase("OPEN")); m.addAttribute("reviewed", detections.countByAlertStatusIgnoreCase("REVIEWED")); m.addAttribute("zoneCount", zones.count()); m.addAttribute("detections", all); return "reports"; }
}
