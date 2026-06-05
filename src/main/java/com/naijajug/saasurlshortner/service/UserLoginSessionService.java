package com.naijajug.saasurlshortner.service;

import java.util.UUID;

public interface UserLoginSessionService {
    void createLoginSession(String sessionId, UUID userId);
    void invalidateLoginSession(UUID userId);
    String getUserSession(UUID userId);
}
