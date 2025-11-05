package com.example.ordermeal.service;

import com.example.ordermeal.entity.AppState;
import com.example.ordermeal.repository.AppStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppStateService {
    private final AppStateRepository appStateRepository;

    public boolean isOrderingLocked() {
        return appStateRepository.findById(1)
                .map(AppState::isOrderingLocked)
                .orElse(false);
    }

    public void setOrderingLocked(boolean isLocked) {
        AppState appState = appStateRepository.findById(1).orElse(new AppState());
        appState.setId(1);
        appState.setOrderingLocked(isLocked);
        appStateRepository.save(appState);
    }
}