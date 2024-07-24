package com.rocketseat.planner.services;

import com.rocketseat.planner.entitys.Trip;
import com.rocketseat.planner.exceptions.DateTimeException;
import com.rocketseat.planner.exceptions.NoSuchElementException;
import com.rocketseat.planner.exceptions.NullPointerException;
import com.rocketseat.planner.repositorys.TripRepository;
import com.rocketseat.planner.requestPayLoads.TripRequestPayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public Trip postCreateTrip(TripRequestPayLoad payLoad) {
        Trip newTrip = new Trip(payLoad);

        if (payLoad.destination()==null || payLoad.starts_at()==null || payLoad.ends_at()==null || payLoad.owner_email()==null || payLoad.owner_name()==null){
            throw new NullPointerException("All fields are mandatory");
        }

        if (payLoad.starts_at().isAfter(payLoad.ends_at()) || payLoad.ends_at().isBefore(payLoad.starts_at())){
            throw new DateTimeException("Check the dates");

        }

        this.tripRepository.save(newTrip);
        return newTrip;
    }

    public Trip getTripDetails(UUID tripId) {
        Optional<Trip> newTrip = tripRepository.findById(tripId);
        return newTrip.orElseThrow(() -> new NoSuchElementException(tripId));
    }

    public Trip updateTrip(UUID id, TripRequestPayLoad payLoad) {
        Optional<Trip> newTrip = this.tripRepository.findById(id);
        Trip rawTrip = newTrip.get();

        rawTrip.setEndsAt(payLoad.ends_at());
        rawTrip.setStartsAt(payLoad.starts_at());
        rawTrip.setDestination(payLoad.destination());

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> newTrip = this.tripRepository.findById(id);
        Trip rawTrip = newTrip.get();

        rawTrip.setConfirmed(true);

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }
}
