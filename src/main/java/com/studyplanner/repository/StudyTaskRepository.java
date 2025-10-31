package com.studyplanner.repository;

import com.studyplanner.entity.StudyTask;
import com.studyplanner.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    
    List<StudyTask> findByUserId(Long userId);
    
    List<StudyTask> findByUserIdAndStatus(Long userId, TaskStatus status);
    
    List<StudyTask> findByUserIdAndSubjectId(Long userId, Long subjectId);
    
    List<StudyTask> findByUserIdAndDueDateBefore(Long userId, LocalDate date);
    
    Long countByUserIdAndStatus(Long userId, TaskStatus status);
}
