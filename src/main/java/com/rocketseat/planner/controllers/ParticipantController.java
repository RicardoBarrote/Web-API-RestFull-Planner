package com.rocketseat.planner.controllers;

import com.rocketseat.planner.entitys.Participant;
import com.rocketseat.planner.repositorys.ParticipantRepository;
import com.rocketseat.planner.requestPayLoads.ParticipantRequestPayLoad;
import com.rocketseat.planner.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    ParticipantService participantService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayLoad payLoad) {
        Participant newParticipant = this.participantService.confirmParticipant(id, payLoad);
        return ResponseEntity.ok(newParticipant);
    }
}
