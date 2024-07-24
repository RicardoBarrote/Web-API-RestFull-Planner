package com.rocketseat.planner.controllers;

import com.rocketseat.planner.entitys.Activity;
import com.rocketseat.planner.entitys.Link;
import com.rocketseat.planner.entitys.Participant;
import com.rocketseat.planner.entitys.Trip;
import com.rocketseat.planner.exceptions.NoSuchElementException;
import com.rocketseat.planner.repositorys.TripRepository;
import com.rocketseat.planner.requestPayLoads.ActivityRequestPayLoad;
import com.rocketseat.planner.requestPayLoads.LinkRequestPayLoad;
import com.rocketseat.planner.requestPayLoads.ParticipantRequestPayLoad;
import com.rocketseat.planner.requestPayLoads.TripRequestPayLoad;
import com.rocketseat.planner.responses.ActivityResponse;
import com.rocketseat.planner.responses.LinkResponse;
import com.rocketseat.planner.responses.ParticipantCreateResponse;
import com.rocketseat.planner.responses.TripCreateResponse;
import com.rocketseat.planner.services.ActivityService;
import com.rocketseat.planner.services.LinkService;
import com.rocketseat.planner.services.ParticipantService;
import com.rocketseat.planner.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private TripService tripService;

    //REQUISIÇÕES HTTP PARA TRIP ↓↓


    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payload) {
        Trip newTrip = this.tripService.postCreateTrip(payload);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    //Recuperar detalhes da viagem, FALTA PENDURAR AS ATIVIDADES E TRATAR EXCEÇÕES.
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Trip trip = this.tripService.getTripDetails(id);
        return ResponseEntity.ok(trip);
    }

    //Atualizar dados da viagem, FALTA TRATAR EXCEÇÕES.
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip newTrip = this.tripService.updateTrip(id, payLoad);
            return ResponseEntity.ok(newTrip);
        }
        return ResponseEntity.notFound().build();
    }

    //Confirmar viagem, FALTA TRATAR EXCEÇÕES.
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip newTrip = this.tripService.confirmTrip(id);
            this.participantService.triggerConfirmationEmailToParticipant(id);

            return ResponseEntity.ok(newTrip);
        }
        return ResponseEntity.notFound().build();
    }

    //REQUISIÇÕES HTTP PARA ACTIVITY ↓↓

    //Registrar atividade, OK.
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ActivityResponse activityResponse = this.activityService.registerActivity(payLoad, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }
        throw new NoSuchElementException(id);
    }

    //Pegar todas as atividades da viagem
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<Activity>> getAllActivities(@PathVariable UUID id) {
        List<Activity> activityList = this.activityService.getAllActivitiesFromId(id);
        return ResponseEntity.ok(activityList);
    }

    //REQUISIÇÕES HTTP PARA PARTICIPANT ↓↓
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ParticipantCreateResponse participantCreateResponse = this.participantService.registerParticipantToEvent(payLoad.email(), rawTrip);

            if (rawTrip.getConfirmed()) {
                this.participantService.triggerConfirmationEmailToParticipant(payLoad.email());
            }
            return ResponseEntity.ok(participantCreateResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Participant>> getAllParticipant(@PathVariable UUID id) {
        List<Participant> particpantList = this.participantService.getAllParticipantsFromEvent(id);
        return ResponseEntity.ok(particpantList);
    }

    //REQUISIÇÕES HTTP PARA LINK ↓↓
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payLoad, rawTrip);
            return ResponseEntity.ok(linkResponse);
        }
        throw new NoSuchElementException(id);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<Link>> getAllLinks(@PathVariable UUID id) {
        List<Link> linkData = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(linkData);
    }
}
