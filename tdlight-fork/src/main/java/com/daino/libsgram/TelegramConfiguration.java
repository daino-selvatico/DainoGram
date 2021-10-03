package com.daino.libsgram;

import org.drinkless.td.libcore.telegram.Client;

import java.util.concurrent.Semaphore;

import it.tdlight.client.SimpleTelegramClient;

public class TelegramConfiguration {
    private static TelegramConfiguration configuration;

    private String authCode = null;
    private Semaphore authSemaphore;
    private boolean needLogin;
    private Semaphore loggedStatusSemaphore;
    private SimpleTelegramClient client;


    public static TelegramConfiguration getInstance() {
        if (configuration == null) {
            configuration = new TelegramConfiguration();
            configuration.authSemaphore = new Semaphore(1);
            configuration.needLogin = true;
            configuration.loggedStatusSemaphore = new Semaphore(1);

        }
        return configuration;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Semaphore getAuthSemaphore() {
        return authSemaphore;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public Semaphore getLoggedStatusSemaphore() {
        return loggedStatusSemaphore;
    }

    public SimpleTelegramClient getClient() {
        return client;
    }

    public void setClient(SimpleTelegramClient client) {
        this.client = client;
    }
}
