package org.example.demo2.service;

import org.example.demo2.entity.EmailSettings;

import java.util.List;

public interface IEmailSettingsService {
    EmailSettings getSettings();
    void save(EmailSettings emailSettings);
    List<String> getAllLanguages();
    List<Integer> getAllPageSizes();
}
