package com.rocketseat.planner.link;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse registerLink (LinkRequestPayLoad payLoad, Trip trip){
        Link newLink = new Link(payLoad.title(), payLoad.url(), trip);
        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }


}
