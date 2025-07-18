package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByHostUsername(String username);
    boolean existsByMeetingCode(String meetingCode);
    
    Meeting findByMeetingCode(String meetingCode); // 🔹 Add this
}


