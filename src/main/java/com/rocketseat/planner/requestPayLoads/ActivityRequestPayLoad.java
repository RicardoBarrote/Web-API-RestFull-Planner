package com.rocketseat.planner.requestPayLoads;

import com.rocketseat.planner.entitys.Trip;

import java.time.LocalDateTime;

public record ActivityRequestPayLoad(Trip trip, String title, LocalDateTime occurs_at) {
}
