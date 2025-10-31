package com.studyplanner.repository;

import com.studyplanner.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    
    List<UserAchievement> findByUserId(Long userId);
    
    Boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}
