package com.studyplanner.service;

import com.studyplanner.entity.Subject;
import com.studyplanner.entity.User;
import com.studyplanner.repository.SubjectRepository;
import com.studyplanner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SubjectService subjectService;

    private User user;
    private Subject subject;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Matemática");
        subject.setDescription("Cálculo");
        subject.setColor("#3B82F6");
        subject.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    }

    @Test
    void testCreateSubject_Success() {
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        Subject created = subjectService.createSubject(subject);

        assertNotNull(created);
        assertEquals("Matemática", created.getName());
        assertEquals(user, created.getUser());
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void testGetUserSubjects_Success() {
        List<Subject> subjects = Arrays.asList(subject);
        when(subjectRepository.findByUserId(anyLong())).thenReturn(subjects);

        List<Subject> result = subjectService.getUserSubjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matemática", result.get(0).getName());
    }

    @Test
    void testGetSubjectById_Success() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject));

        Subject found = subjectService.getSubjectById(1L);

        assertNotNull(found);
        assertEquals("Matemática", found.getName());
    }

    @Test
    void testGetSubjectById_NotFound() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            subjectService.getSubjectById(999L);
        });

        assertEquals("Matéria não encontrada", exception.getMessage());
    }

    @Test
    void testDeleteSubject_Success() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject));

        subjectService.deleteSubject(1L);

        verify(subjectRepository, times(1)).delete(subject);
    }
}
