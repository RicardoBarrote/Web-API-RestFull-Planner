package com.rocketseat.planner.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public Trip createTrip(TripRequestPayLoad payLoad) {
        Trip newTrip = new Trip(payLoad);
        this.tripRepository.save(newTrip);

        return newTrip;
    }

    public Optional<Trip> tripDetails(UUID tripId) {
        return this.tripRepository.findById(tripId);
    }

    public Trip updateTrip(UUID id, TripRequestPayLoad payLoad) {
        Optional<Trip> newTrip = this.tripRepository.findById(id);
        Trip rawTrip = newTrip.get();

        rawTrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setDestination(payLoad.destination());

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }

    public Trip confirmTrip (UUID id){
        Optional<Trip> newTrip = this.tripRepository.findById(id);
        Trip rawTrip = newTrip.get();

        rawTrip.setConfirmed(true);

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }
}
