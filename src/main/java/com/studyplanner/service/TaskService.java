package com.studyplanner.service;

import com.studyplanner.entity.StudyTask;
import com.studyplanner.entity.TaskStatus;
import com.studyplanner.entity.User;
import com.studyplanner.repository.StudyTaskRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private StudyTaskRepository taskRepository;

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

    public List<StudyTask> getAllTasks() {
        User user = getCurrentUser();
        return taskRepository.findByUserId(user.getId());
    }

    public StudyTask getTaskById(Long id) {
        User user = getCurrentUser();
        StudyTask task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        
        return task;
    }

    public StudyTask createTask(StudyTask task) {
        User user = getCurrentUser();
        task.setUser(user);
        return taskRepository.save(task);
    }

    public StudyTask updateTask(Long id, StudyTask taskDetails) {
        StudyTask task = getTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setDueDate(taskDetails.getDueDate());
        task.setSubject(taskDetails.getSubject());
        task.setTopic(taskDetails.getTopic());
        return taskRepository.save(task);
    }

    public StudyTask updateTaskStatus(Long id, TaskStatus status) {
        StudyTask task = getTaskById(id);
        task.setStatus(status);
        
        if (status == TaskStatus.COMPLETED && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
            gamificationService.addExperience(task.getUser(), 10);
        }
        
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        StudyTask task = getTaskById(id);
        taskRepository.delete(task);
    }

    public List<StudyTask> getTasksByStatus(TaskStatus status) {
        User user = getCurrentUser();
        return taskRepository.findByUserIdAndStatus(user.getId(), status);
    }
}
