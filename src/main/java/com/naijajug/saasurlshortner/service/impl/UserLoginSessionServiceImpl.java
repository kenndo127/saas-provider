package com.naijajug.saasurlshortner.service.impl;

import com.naijajug.saasurlshortner.exceptions.AccessDeniedException;
import com.naijajug.saasurlshortner.model.UserLoginSession;
import com.naijajug.saasurlshortner.repository.UserLoginSessionRepository;
import com.naijajug.saasurlshortner.service.UserLoginSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLoginSessionServiceImpl implements UserLoginSessionService {

    private final UserLoginSessionRepository userLoginSessionRepository;

    @Override
    @Transactional
    public void createLoginSession(String sessionId, UUID userId) {
        Optional<UserLoginSession> activeSessionOptional = userLoginSessionRepository.findByUserId(userId);
        if (activeSessionOptional.isEmpty()){
            UserLoginSession newUserSession = UserLoginSession.builder()
                    .activeSessionId(sessionId)
                    .userId(userId)
                    .build();
            userLoginSessionRepository.save(newUserSession);
        }else{
            UserLoginSession activeSession = activeSessionOptional.get();
            activeSession.setActiveSessionId(sessionId);
            userLoginSessionRepository.save(activeSession);
        }
    }

    @Override
    @Transactional
    public void invalidateLoginSession(UUID userId) {
        Optional<UserLoginSession> activeSessionOptional = userLoginSessionRepository.findByUserId(userId);
        if (activeSessionOptional.isPresent()){
            userLoginSessionRepository.deleteByUserId(userId);
        }
    }

    @Override
    public String getUserSession(UUID userId) {
        Optional<UserLoginSession> activeSessionOptional = userLoginSessionRepository.findByUserId(userId);
        if (activeSessionOptional.isEmpty()) throw new AccessDeniedException("Something went wrong!");
        return activeSessionOptional.get().getActiveSessionId();
    }
}
