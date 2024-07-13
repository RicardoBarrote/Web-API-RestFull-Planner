package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);

        System.out.println(participants.get(0).getId());
    }

    public void triggerConfirmationEmailToParticipant(UUID tripId){
    }
    public void triggerConfirmationEmailToParticipant(String email){
    }

    public ParticipantCreateResponse registerParticipantToEvent (String email, Trip trip){
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public List<ParticipantData> getAllParticipantsFromEvent (UUID tripId) {
        return this.participantRepository.findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getConfirmed())).toList();
    }
}
