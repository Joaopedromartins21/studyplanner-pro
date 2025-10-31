package com.studyplanner.service;

import com.studyplanner.entity.Achievement;
import com.studyplanner.entity.User;
import com.studyplanner.entity.UserAchievement;
import com.studyplanner.repository.AchievementRepository;
import com.studyplanner.repository.UserAchievementRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GamificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    public void addExperience(User user, Integer xp) {
        user.setExperience(user.getExperience() + xp);
        
        // Sistema de níveis: cada nível requer 100 XP * nível atual
        int requiredXp = user.getLevel() * 100;
        while (user.getExperience() >= requiredXp) {
            user.setExperience(user.getExperience() - requiredXp);
            user.setLevel(user.getLevel() + 1);
            requiredXp = user.getLevel() * 100;
        }
        
        userRepository.save(user);
        checkAchievements(user);
    }

    private void checkAchievements(User user) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        
        for (Achievement achievement : allAchievements) {
            boolean hasAchievement = userAchievementRepository.existsByUserIdAndAchievementId(
                    user.getId(), achievement.getId());
            
            if (!hasAchievement && user.getLevel() >= achievement.getRequiredExperience()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUser(user);
                userAchievement.setAchievement(achievement);
                userAchievement.setUnlockedAt(LocalDateTime.now());
                userAchievementRepository.save(userAchievement);
            }
        }
    }

    public List<UserAchievement> getUserAchievements(Long userId) {
        return userAchievementRepository.findByUserId(userId);
    }
}
