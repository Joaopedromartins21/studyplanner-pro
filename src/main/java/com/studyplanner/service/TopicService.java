package com.studyplanner.service;

import com.studyplanner.entity.Topic;
import com.studyplanner.entity.User;
import com.studyplanner.repository.TopicRepository;
import com.studyplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Topic> getTopicsBySubject(Long subjectId) {
        return topicRepository.findBySubjectId(subjectId);
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
    }

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, Topic topicDetails) {
        Topic topic = getTopicById(id);
        topic.setName(topicDetails.getName());
        topic.setDescription(topicDetails.getDescription());
        topic.setCompleted(topicDetails.getCompleted());
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        Topic topic = getTopicById(id);
        topicRepository.delete(topic);
    }
}
