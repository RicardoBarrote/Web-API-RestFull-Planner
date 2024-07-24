package com.rocketseat.planner.services;

import com.rocketseat.planner.entitys.Link;
import com.rocketseat.planner.exceptions.NoSuchElementException;
import com.rocketseat.planner.entitys.Trip;
import com.rocketseat.planner.repositorys.LinkRepository;
import com.rocketseat.planner.requestPayLoads.LinkRequestPayLoad;
import com.rocketseat.planner.responses.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse registerLink(LinkRequestPayLoad payLoad, Trip trip) {
        Link newLink = new Link(payLoad.title(), payLoad.url(), trip);
        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<Link> getAllLinksFromTrip(UUID tripId) {
        Optional<Link> linkId = this.linkRepository.findById(tripId);

        if (linkId == null) {
            linkId.orElseThrow(() -> new NoSuchElementException(tripId));
        }
        return this.linkRepository.findByTripId(tripId)
                .stream()
                .map(link -> new Link(link.getId(), link.getTitle(), link.getUrl(), link.getTrip())).toList();
    }
}
