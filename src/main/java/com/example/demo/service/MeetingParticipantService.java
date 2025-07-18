package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Meeting;
import com.example.demo.model.MeetingParticipant;
import com.example.demo.model.ParticipantRole;
import com.example.demo.repository.MeetingParticipantRepository;
import com.example.demo.repository.MeetingRepository;
import com.example.demo.repository.UserRepository;

@Service
public class MeetingParticipantService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingParticipantRepository participantRepository;

    public MeetingParticipant joinMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new RuntimeException("Meeting not found"));

        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (participantRepository.existsByMeetingAndUser(meeting, user)) {
            throw new RuntimeException("User already joined this meeting");
        }

        // 🔁 Use username comparison instead of getHost().getId()
        ParticipantRole role = meeting.getHostUsername().equals(user.getUsername()) ?
                ParticipantRole.HOST : ParticipantRole.PARTICIPANT;

        MeetingParticipant participant = new MeetingParticipant();
        participant.setUser(user);
        participant.setMeeting(meeting);
        participant.setRole(role);
        participant.setJoinedAt(LocalDateTime.now());

        return participantRepository.save(participant);
    }

    public ParticipantRole getUserRoleInMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new RuntimeException("Meeting not found"));

        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return participantRepository.findByMeetingAndUser(meeting, user)
            .orElseThrow(() -> new RuntimeException("User not in meeting"))
            .getRole();
    }

    // ✅ Promote a participant to co-host (compare host by username)
    public MeetingParticipant promoteToCoHost(Long meetingId, Long hostUserId, Long targetUserId) {
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new RuntimeException("Meeting not found"));

        AppUser hostUser = userRepository.findById(hostUserId)
            .orElseThrow(() -> new RuntimeException("Host user not found"));

        if (!meeting.getHostUsername().equals(hostUser.getUsername())) {
            throw new RuntimeException("Only the host can promote users to co-host");
        }

        AppUser targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new RuntimeException("Target user not found"));

        MeetingParticipant participant = participantRepository.findByMeetingAndUser(meeting, targetUser)
            .orElseThrow(() -> new RuntimeException("Target user is not in the meeting"));

        if (participant.getRole() == ParticipantRole.CO_HOST) {
            throw new RuntimeException("User is already a co-host");
        }

        participant.setRole(ParticipantRole.CO_HOST);
        return participantRepository.save(participant);
    }
}
