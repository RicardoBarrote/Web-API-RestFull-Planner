package com.rocketseat.planner.services;

import com.rocketseat.planner.entitys.Participant;
import com.rocketseat.planner.entitys.Trip;
import com.rocketseat.planner.repositorys.ParticipantRepository;
import com.rocketseat.planner.responses.ParticipantCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);

        System.out.println(participants.get(0).getId());
    }

    public void triggerConfirmationEmailToParticipant(UUID tripId) {
    }

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public List<Participant> getAllParticipantsFromEvent(UUID tripId) {
        return this.participantRepository.findByTripId(tripId)
                .stream()
                .map(participant -> new Participant(participant.getId(), participant.getName(), participant.getEmail(), participant.getConfirmed())).toList();
    }

}
