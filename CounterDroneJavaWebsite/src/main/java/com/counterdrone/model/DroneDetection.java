
package com.counterdrone.repository;

import com.counterdrone.model.DroneDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneDetectionRepository
        extends JpaRepository<DroneDetection, Long> {

    List<DroneDetection> findAllByOrderByDetectedAtDesc();

    long countByAlertStatusIgnoreCase(String alertStatus);

    long countByThreatLevelIgnoreCase(String threatLevel);
}
