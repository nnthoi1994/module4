package org.example.demo2.repository;

import org.example.demo2.entity.EmailSettings;
import org.springframework.stereotype.Repository;

@Repository
public class EmailSettingsRepository implements IEmailSettingsRepository{
    private static EmailSettings currentSettings = new EmailSettings("English", 25, true, "Thor\nKing, Asgard");
    @Override
    public EmailSettings getSettings() {
        return currentSettings;
    }

    @Override
    public void updateSettings(EmailSettings emailSettings) {
        currentSettings = emailSettings;
    }
}
