package com.studyplanner.repository;

import com.studyplanner.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    
    List<StudySession> findByUserId(Long userId);
    
    List<StudySession> findByUserIdAndSubjectId(Long userId, Long subjectId);
    
    @Query("SELECT s FROM StudySession s WHERE s.user.id = :userId AND s.startTime >= :startDate AND s.startTime <= :endDate")
    List<StudySession> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(s.duration), 0) FROM StudySession s WHERE s.user.id = :userId AND s.startTime >= :startDate")
    Integer getTotalStudyTimeByUserIdSince(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
}
