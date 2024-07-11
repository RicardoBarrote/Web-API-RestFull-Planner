package com.rocketseat.planner.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
* Interface JPA, onde estabelece comunicação com o banco de dados e manipulação.
* Efetuar as Query no banco e etc
*/
public interface TripRepository extends JpaRepository<Trip, UUID> {
}
