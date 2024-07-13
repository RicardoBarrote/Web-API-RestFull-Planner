package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayLoad;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.*;
import com.rocketseat.planner.participant.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    //REQUISIÇÕES HTTP PARA TRIP ↓↓

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payload) {
    Trip newTrip = new Trip(payload);
    this.tripRepository.save(newTrip);

    this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
    return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
   }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails (@PathVariable UUID id){
        Optional<Trip> trip = this.tripRepository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayLoad payLoad){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payLoad.destination());

            this.tripRepository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);

        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipant(id);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    //REQUISIÇÕES HTTP PARA ACTIVITY ↓↓

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayLoad payLoad){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payLoad, rawTrip);
            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);
        return ResponseEntity.ok(activityDataList);
    }

    //REQUISIÇÕES HTTP PARA PARTICIPANT ↓↓

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ParticipantCreateResponse participantCreateResponse = this.participantService.registerParticipantToEvent(payLoad.email(), rawTrip);

            if (rawTrip.getConfirmed()){
                this.participantService.triggerConfirmationEmailToParticipant(payLoad.email());
            }
            return ResponseEntity.ok(participantCreateResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipant(@PathVariable UUID id){
        List<ParticipantData> particpantList = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(particpantList);
    }

    //REQUISIÇÕES HTTP PARA LINK ↓↓

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayLoad payLoad) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()){
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payLoad, rawTrip);
            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){
        List<LinkData> linkData = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkData);
    }

}
