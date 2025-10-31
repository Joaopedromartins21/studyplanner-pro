package com.studyplanner.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.studyplanner.entity.StudyTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleCalendarService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private static final String APPLICATION_NAME = "StudyPlanner Pro";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public String syncTaskToCalendar(StudyTask task, String accessToken) {
        try {
            Calendar service = getCalendarService(accessToken);

            Event event = new Event()
                    .setSummary(task.getTitle())
                    .setDescription(task.getDescription());

            if (task.getDueDate() != null) {
                Date dueDate = Date.from(task.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                EventDateTime start = new EventDateTime()
                        .setDateTime(new com.google.api.client.util.DateTime(dueDate));
                event.setStart(start);

                EventDateTime end = new EventDateTime()
                        .setDateTime(new com.google.api.client.util.DateTime(dueDate));
                event.setEnd(end);
            }

            event = service.events().insert("primary", event).execute();
            return event.getId();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao sincronizar com Google Calendar: " + e.getMessage());
        }
    }

    private Calendar getCalendarService(String accessToken) throws GeneralSecurityException, IOException {
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                request -> request.getHeaders().setAuthorization("Bearer " + accessToken))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
