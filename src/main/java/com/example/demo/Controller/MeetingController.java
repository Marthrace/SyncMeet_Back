package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Meeting;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.MeetingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    
    @PostMapping("/join/{meetingId}")
public ResponseEntity<?> joinMeetingByEmail(@PathVariable Long meetingId, @RequestBody(required = true) java.util.Map<String, String> payload) {
    String email = payload.get("email");

    Optional<Meeting> meetingOpt = meetingService.getMeetingById(meetingId);
    if (meetingOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found");
    }

    Meeting meeting = meetingOpt.get();

    if (!meeting.getParticipantsEmails().contains(email)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email is not invited to this meeting.");
    }

    return ResponseEntity.ok("Successfully joined as: " + email);
}


    @Autowired
    private MeetingService meetingService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/my")
public ResponseEntity<List<Meeting>> getMyMeetings(HttpServletRequest request) {
    String username = jwtUtil.extractUsernameFromRequest(request);
    List<Meeting> meetings = meetingService.getMeetingsByHost(username);
    return ResponseEntity.ok(meetings);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id, HttpServletRequest request) {
        String username = jwtUtil.extractUsernameFromRequest(request);
        boolean deleted = meetingService.deleteMeetingIfHost(id, username);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the host can delete this meeting.");
        }
    }

    @GetMapping("/join/{code}")
    public ResponseEntity<?> joinMeeting(@PathVariable String code) {
        Optional<Meeting> meeting = meetingService.getMeetingByCode(code);
        if (meeting.isPresent()) {
            return ResponseEntity.ok(meeting.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Meeting not found with code: " + code);
        }
    }

    @PostMapping("/schedule")
    public ResponseEntity<Meeting> scheduleMeeting(@RequestBody Meeting meeting) {
        return ResponseEntity.ok(meetingService.scheduleMeeting(meeting));
    }

    @GetMapping("/host/{username}")
    public ResponseEntity<List<Meeting>> getMeetingsByHost(@PathVariable String username) {
        return ResponseEntity.ok(meetingService.getMeetingsByHost(username));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Meeting> getMeetingByCode(@PathVariable String code) {
        return meetingService.getMeetingByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
}
