package com.rocketseat.planner.link;

import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Link(){
        super();
    }

    public Link(UUID id, String url, String title, Trip trip) {
        super();
        this.id = id;
        this.url = url;
        this.title = title;
        this.trip = trip;
    }

    public Link(String title, String url, Trip trip) {
        this.title = title;
        this.url = url;
        this.trip = trip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
