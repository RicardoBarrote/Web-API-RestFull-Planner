package com.rocketseat.planner.repositorys;

import com.rocketseat.planner.entitys.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
