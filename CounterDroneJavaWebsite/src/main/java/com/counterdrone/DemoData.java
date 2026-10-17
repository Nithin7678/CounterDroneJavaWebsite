package com.counterdrone;
import com.counterdrone.model.SurveillanceZone;
import com.counterdrone.repository.SurveillanceZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DemoData {
 @Bean CommandLineRunner seedZones(SurveillanceZoneRepository zones) { return args -> {
  if(zones.count()==0) {
   zones.save(new SurveillanceZone("International Airport", "Critical", true));
   zones.save(new SurveillanceZone("North Gate", "High", true));
   zones.save(new SurveillanceZone("Power Infrastructure", "High", true));
   zones.save(new SurveillanceZone("Defence Perimeter", "Critical", true));
  }
 }; }
}
