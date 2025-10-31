package org.example.demo2.service;

import org.example.demo2.entity.EmailSettings;
import org.example.demo2.repository.IEmailSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailSettingService implements IEmailSettingsService {
    @Autowired
    private IEmailSettingsRepository emailSettingsRepository;
    @Override
    public EmailSettings getSettings() {
        return emailSettingsRepository.getSettings();
    }

    @Override
    public void save(EmailSettings emailSettings) {
        emailSettingsRepository.updateSettings(emailSettings);
    }

    @Override
    public List<String> getAllLanguages() {
        return Arrays.asList("English", "Vietnamese", "Japanese", "Chinese");
    }

    @Override
    public List<Integer> getAllPageSizes() {
        return Arrays.asList(5, 10, 15, 25, 50, 100);
    }
}
