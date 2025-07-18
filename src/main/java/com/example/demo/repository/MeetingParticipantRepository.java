package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AppUser;
import com.example.demo.model.Meeting;
import com.example.demo.model.MeetingParticipant;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, AppUser user);
    Optional<MeetingParticipant> findByMeetingAndUser(Meeting meeting, AppUser user);
    List<MeetingParticipant> findAllByMeeting(Meeting meeting);
}
