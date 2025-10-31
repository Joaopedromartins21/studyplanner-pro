package com.studyplanner.controller;

import com.studyplanner.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/study-plan")
    public ResponseEntity<Map<String, Object>> getStudyRecommendations() {
        return ResponseEntity.ok(recommendationService.getStudyRecommendations());
    }
}
