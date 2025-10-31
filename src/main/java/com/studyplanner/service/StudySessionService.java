package com.studyplanner.service;

import com.studyplanner.entity.StudySession;
import com.studyplanner.entity.User;
import com.studyplanner.repository.StudySessionRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GamificationService gamificationService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public StudySession startSession(StudySession session) {
        User user = getCurrentUser();
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public StudySession endSession(Long id) {
        User user = getCurrentUser();
        StudySession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));
        
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        
        session.setEndTime(LocalDateTime.now());
        long minutes = ChronoUnit.MINUTES.between(session.getStartTime(), session.getEndTime());
        session.setDuration((int) minutes);
        
        // Adicionar XP: 5 XP por pomodoro completo (25 min)
        int xpGained = session.getPomodoroCount() * 5;
        gamificationService.addExperience(user, xpGained);
        
        return sessionRepository.save(session);
    }

    public List<StudySession> getAllSessions() {
        User user = getCurrentUser();
        return sessionRepository.findByUserId(user.getId());
    }

    public Map<String, Object> getWeeklyStats() {
        User user = getCurrentUser();
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        
        List<StudySession> sessions = sessionRepository.findByUserIdAndDateRange(
                user.getId(), weekAgo, LocalDateTime.now());
        
        int totalMinutes = sessions.stream()
                .mapToInt(StudySession::getDuration)
                .sum();
        
        int totalPomodoros = sessions.stream()
                .mapToInt(StudySession::getPomodoroCount)
                .sum();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMinutes", totalMinutes);
        stats.put("totalHours", totalMinutes / 60.0);
        stats.put("totalPomodoros", totalPomodoros);
        stats.put("sessionsCount", sessions.size());
        
        return stats;
    }
}
