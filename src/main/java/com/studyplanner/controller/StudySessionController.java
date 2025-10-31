package com.studyplanner.controller;

import com.studyplanner.entity.StudySession;
import com.studyplanner.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class StudySessionController {

    @Autowired
    private StudySessionService sessionService;

    @PostMapping("/start")
    public ResponseEntity<StudySession> startSession(@RequestBody StudySession session) {
        return ResponseEntity.ok(sessionService.startSession(session));
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<StudySession> endSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.endSession(id));
    }

    @GetMapping
    public ResponseEntity<List<StudySession>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/weekly-stats")
    public ResponseEntity<Map<String, Object>> getWeeklyStats() {
        return ResponseEntity.ok(sessionService.getWeeklyStats());
    }
}
