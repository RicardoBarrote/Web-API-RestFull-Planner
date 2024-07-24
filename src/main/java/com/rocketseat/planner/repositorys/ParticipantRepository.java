package com.rocketseat.planner.repositorys;

import com.rocketseat.planner.entitys.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant,  UUID> {
    List<Participant> findByTripId(UUID id);
}
