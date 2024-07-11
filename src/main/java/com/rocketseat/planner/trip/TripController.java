package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripRepository tripRepository;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payload) {
    Trip newTrip = new Trip(payload);
    this.tripRepository.save(newTrip);
    this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip.getId());
    return ResponseEntity.ok().body(new TripCreateResponse(newTrip.getId()));
};


    //Explicando a expressão Lambda abaixo.
    //return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    /*trip.map(ResponseEntity::ok) -> Verifica se existe uma informação dentro do trip,
     *caso contenha algo, ele monta uma resposta HTTP ResponseEntity com o estado ok*/

    /*Caso a resposta for nulla, não exista um id correspondente,
     * ele monta uma ResponseEntity com o estado .notFound().build()*/

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails (@PathVariable UUID id){

        Optional<Trip> trip = this.tripRepository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    };

}