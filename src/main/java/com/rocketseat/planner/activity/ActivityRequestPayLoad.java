package com.rocketseat.planner.activity;

import java.time.LocalDateTime;

public record ActivityRequestPayLoad(String title, LocalDateTime occurs_at) {
}
