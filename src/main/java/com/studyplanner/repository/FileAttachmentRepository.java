package com.studyplanner.repository;

import com.studyplanner.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    
    List<FileAttachment> findByUserId(Long userId);
    
    List<FileAttachment> findByNoteId(Long noteId);
    
    List<FileAttachment> findByTaskId(Long taskId);
}
