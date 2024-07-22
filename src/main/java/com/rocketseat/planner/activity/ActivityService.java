package com.rocketseat.planner.activity;

import com.rocketseat.planner.controller.exceptions.NullPointerException;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayLoad payLoad, Trip trip) {
        Activity newActivity = new Activity(payLoad.title(), payLoad.occurs_at(), trip);

        if (payLoad.occurs_at() == null) {
            throw new NullPointerException("Data é um campo obrigatório");
        }
        if (payLoad.title() == null) {
            throw new NullPointerException("Login é um campo obrigatório");
        }

        this.activityRepository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.activityRepository
                .findByTripId(tripId)
                .stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}