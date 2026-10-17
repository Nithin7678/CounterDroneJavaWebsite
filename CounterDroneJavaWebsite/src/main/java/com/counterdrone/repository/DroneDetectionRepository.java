package com.counterdrone.repository;

import com.counterdrone.model.DroneDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneDetectionRepository extends JpaRepository<DroneDetection, Long> {

    List<DroneDetection> findAllByOrderByDetectedAtDesc();

    List<DroneDetection> findByThreatLevelNotIgnoreCaseOrderByDetectedAtDesc(String threatLevel);

    long countByThreatLevelIgnoreCase(String threatLevel);

    long countByAlertStatusIgnoreCase(String alertStatus);
}
