package com.rocketseat.planner.services;

import com.rocketseat.planner.repositorys.ActivityRepository;
import com.rocketseat.planner.requestPayLoads.ActivityRequestPayLoad;
import com.rocketseat.planner.responses.ActivityResponse;
import com.rocketseat.planner.entitys.Activity;
import com.rocketseat.planner.exceptions.NoSuchElementException;
import com.rocketseat.planner.exceptions.NullPointerException;
import com.rocketseat.planner.entitys.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayLoad payLoad, Trip trip) {
        Activity newActivity = new Activity(payLoad.title(), payLoad.occurs_at(), trip);

        if (payLoad.occurs_at() == null || payLoad.title()==null) {
            throw new NullPointerException("Date and title are mandatory fields");
        }
        this.activityRepository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<Activity> getAllActivitiesFromId(UUID tripId) {
        Optional<Activity> activityId = this.activityRepository.findById(tripId);

        if (activityId == null) {
            activityId.orElseThrow(() -> new NoSuchElementException(tripId));
        }
        return this.activityRepository.findByTripId(tripId)
                .stream()
                .map(activity -> new Activity(activity.getId(), activity.getOccursAt(), activity.getTitle(), activity.getTrip()))
                .toList();
    }
}