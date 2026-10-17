package com.counterdrone.repository;

import com.counterdrone.model.SurveillanceZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveillanceZoneRepository extends JpaRepository<SurveillanceZone, Long> {

    List<SurveillanceZone> findAllByOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}
