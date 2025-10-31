package com.studyplanner.service;

import com.studyplanner.entity.StudySession;
import com.studyplanner.entity.Subject;
import com.studyplanner.entity.User;
import com.studyplanner.repository.StudySessionRepository;
import com.studyplanner.repository.SubjectRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudySessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Map<String, Object> getStudyRecommendations() {
        User user = getCurrentUser();
        List<Subject> subjects = subjectRepository.findByUserId(user.getId());
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        List<StudySession> sessions = sessionRepository.findByUserIdAndDateRange(
                user.getId(), weekAgo, LocalDateTime.now());

        // Calcular tempo por matéria
        Map<Long, Integer> timePerSubject = new HashMap<>();
        for (StudySession session : sessions) {
            Long subjectId = session.getSubject().getId();
            timePerSubject.put(subjectId, 
                timePerSubject.getOrDefault(subjectId, 0) + session.getDuration());
        }

        // Encontrar matérias com menos tempo de estudo
        List<Subject> neglectedSubjects = subjects.stream()
                .filter(subject -> timePerSubject.getOrDefault(subject.getId(), 0) < 60)
                .collect(Collectors.toList());

        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("neglectedSubjects", neglectedSubjects);
        recommendations.put("totalStudyTime", timePerSubject.values().stream().mapToInt(Integer::intValue).sum());
        recommendations.put("message", neglectedSubjects.isEmpty() 
            ? "Ótimo trabalho! Você está estudando todas as matérias regularmente." 
            : "Considere dedicar mais tempo às matérias com menos horas de estudo.");

        return recommendations;
    }
}
