package com.daino.dainogram;

public class TelegramConfiguration {
    private TelegramConfiguration configuration;

    public TelegramConfiguration getInstance() {
        if(configuration == null) {
            configuration = new TelegramConfiguration();
        }
        return configuration;
    }
}
