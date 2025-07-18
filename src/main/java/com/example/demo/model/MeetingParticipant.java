package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MeetingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    private LocalDateTime joinedAt;

    // ✅ Getters

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public ParticipantRole getRole() {
        return role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    // ✅ Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
