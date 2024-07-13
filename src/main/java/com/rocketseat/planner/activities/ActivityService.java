package com.rocketseat.planner.activities;

import com.rocketseat.planner.participant.ParticipantData;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayLoad payLoad, Trip trip){
        Activity newActivity = new Activity(payLoad.title(), payLoad.occurs_at(), trip);
        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData>getAllActivitiesFromId(UUID tripId){
        return this.activityRepository
                .findByTripId(tripId)
                .stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
