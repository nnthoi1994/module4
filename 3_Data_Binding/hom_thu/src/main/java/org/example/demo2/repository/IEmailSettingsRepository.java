package org.example.demo2.repository;

import org.example.demo2.entity.EmailSettings;

public interface IEmailSettingsRepository {
    EmailSettings getSettings();
    void updateSettings(EmailSettings emailSettings);
}
