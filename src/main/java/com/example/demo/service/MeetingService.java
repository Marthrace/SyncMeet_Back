package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Meeting;
import com.example.demo.repository.MeetingRepository;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public boolean deleteMeetingIfHost(Long id, String username) {
    Optional<Meeting> optionalMeeting = meetingRepository.findById(id);
    if (optionalMeeting.isPresent()) {
        Meeting meeting = optionalMeeting.get();
        if (meeting.getHostUsername().equals(username)) {
            meetingRepository.deleteById(id);
            return true;
        }
    }
    return false;
}


    public Meeting scheduleMeeting(Meeting meeting) {
        String code = generateUniqueCode();
        meeting.setMeetingCode(code);
        return meetingRepository.save(meeting);
    }

    public List<Meeting> getMeetingsByHost(String username) {
        return meetingRepository.findByHostUsername(username);
    }

    public Optional<Meeting> getMeetingByCode(String code) {
        return meetingRepository.findAll().stream()
            .filter(m -> m.getMeetingCode().equals(code)).findFirst();
    }

    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 8);
        } while (meetingRepository.existsByMeetingCode(code));
        return code;
    }
    public Optional<Meeting> getMeetingById(Long id) {
    return meetingRepository.findById(id);
}

}
