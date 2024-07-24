package com.rocketseat.planner.requestPayLoads;

import java.time.LocalDateTime;

public record TripRequestPayLoad(String destination, LocalDateTime starts_at, LocalDateTime ends_at, String owner_email, String owner_name) {
}
