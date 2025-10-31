package com.studyplanner.controller;

import com.studyplanner.entity.Achievement;
import com.studyplanner.entity.User;
import com.studyplanner.entity.UserAchievement;
import com.studyplanner.repository.AchievementRepository;
import com.studyplanner.repository.UserRepository;
import com.studyplanner.service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private GamificationService gamificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        return ResponseEntity.ok(achievementRepository.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserAchievement>> getUserAchievements() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(gamificationService.getUserAchievements(user.getId()));
    }
}
