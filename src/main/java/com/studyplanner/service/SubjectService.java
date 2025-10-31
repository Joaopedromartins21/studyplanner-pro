package com.studyplanner.service;

import com.studyplanner.entity.Subject;
import com.studyplanner.entity.User;
import com.studyplanner.repository.SubjectRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Subject> getAllSubjects() {
        User user = getCurrentUser();
        return subjectRepository.findByUserId(user.getId());
    }

    public Subject getSubjectById(Long id) {
        User user = getCurrentUser();
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
        
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        
        return subject;
    }

    public Subject createSubject(Subject subject) {
        User user = getCurrentUser();
        subject.setUser(user);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = getSubjectById(id);
        subject.setName(subjectDetails.getName());
        subject.setDescription(subjectDetails.getDescription());
        subject.setColor(subjectDetails.getColor());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
    }
}
