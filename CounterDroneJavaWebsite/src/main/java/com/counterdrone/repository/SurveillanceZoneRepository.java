package com.counterdrone.repository;
import com.counterdrone.model.SurveillanceZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SurveillanceZoneRepository extends JpaRepository<SurveillanceZone, Long> {
    List<SurveillanceZone> findAllByOrderByNameAsc();
    boolean existsByNameIgnoreCase(String name);
}
