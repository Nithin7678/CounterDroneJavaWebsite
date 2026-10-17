package com.counterdrone.repository;
import com.counterdrone.model.DroneDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DroneDetectionRepository extends JpaRepository<DroneDetection, Long> {
    List<DroneDetection> findAllByOrderByDetectedAtDesc();
    List<DroneDetection> findByThreatLevelNotIgnoreCaseOrderByDetectedAtDesc(String threatLevel);
    long countByThreatLevelIgnoreCase(String threatLevel);
    long countByAlertStatusIgnoreCase(String alertStatus);
}
