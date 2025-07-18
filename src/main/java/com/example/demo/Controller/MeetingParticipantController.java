package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.MeetingParticipant;
import com.example.demo.model.ParticipantRole;
import com.example.demo.service.MeetingParticipantService;

@RestController
@RequestMapping("/api/participants")
public class MeetingParticipantController {

    @Autowired
    private MeetingParticipantService participantService;

    @PostMapping("/join")
    public ResponseEntity<?> joinMeeting(@RequestParam Long meetingId, @RequestParam Long userId) {
        MeetingParticipant participant = participantService.joinMeeting(meetingId, userId);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole(@RequestParam Long meetingId, @RequestParam Long userId) {
        ParticipantRole role = participantService.getUserRoleInMeeting(meetingId, userId);
        return ResponseEntity.ok(role);
    }

    // ✅ NEW ENDPOINT: Promote to Co-Host
    @PatchMapping("/promote")
    public ResponseEntity<?> promoteToCoHost(
            @RequestParam Long meetingId,
            @RequestParam Long hostUserId,
            @RequestParam Long targetUserId) {

        MeetingParticipant updated = participantService.promoteToCoHost(meetingId, hostUserId, targetUserId);
        return ResponseEntity.ok("User " + targetUserId + " promoted to CO_HOST");
    }
}
