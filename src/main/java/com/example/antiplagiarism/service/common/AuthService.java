package com.example.antiplagiarism.service.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AuthService {

    private final List<UserSession> SESSIONS = new LinkedList<>();

    private static final long CLEAR_SESSIONS_INTERVAL = 1000 * 30; // 30 seconds

    @PostConstruct
    public void init() {
        runSessionScheduler();
    }

    public void loginUser(String username, String sessionId) {
        SESSIONS.add(new UserSession(username, sessionId));
    }

    public boolean isUserLoggedIn(String username) {
        clearSessions();
        return SESSIONS.stream().anyMatch(session -> session.username.equals(username));
    }

    public UserSession getUserSessionsByUsername(String username) {
        return SESSIONS.stream().filter(session -> session.username.equals(username))
                .findAny().orElseThrow(() -> new EntityNotFoundException("No such session for username: " + username));
    }

    public UserSession getUserSessionBySessionId(String sessionId) {
        return SESSIONS.stream().filter(session -> session.sessionId.equals(sessionId))
                .findAny().orElseThrow(() -> new EntityNotFoundException("No such session for session id: " + sessionId));
    }

    public boolean logoutUser(String username) {
        if (isUserLoggedIn(username)) {
            UserSession userSession = SESSIONS.stream().filter(session -> session.username.equals(username))
                    .findAny().orElse(null);
            if (userSession != null) {
                SESSIONS.remove(userSession);
                return true;
            }
            return false;
        }
        return false;
    }

    private void runSessionScheduler() {
        ConcurrentTaskScheduler scheduler = new ConcurrentTaskScheduler();
        scheduler.scheduleAtFixedRate(this::clearSessions, CLEAR_SESSIONS_INTERVAL);
    }

    private void clearSessions() {
        List<UserSession> sessionsToDelete = new LinkedList<>();
        log.debug("[SESSION] Running sessions clear...");
        SESSIONS.forEach(session -> {
            Date date = new Date();
            if (date.after(new Date(session.loginTime + session.sessionExpirationTime))) {
                sessionsToDelete.add(session);
            }
        });
        SESSIONS.removeAll(sessionsToDelete);
        log.debug("[SESSION] Cleared {} sessions", sessionsToDelete.size());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSession {

        public static final long DEFAULT_SESSION_EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

        private String username;
        private String sessionId;
        private long loginTime;
        private long sessionExpirationTime;

        public UserSession(String username, String sessionId) {
            this.username = username;
            this.sessionId = sessionId;
            this.sessionExpirationTime = DEFAULT_SESSION_EXPIRATION_TIME;
            this.loginTime = new Date().getTime();
        }

    }
}
