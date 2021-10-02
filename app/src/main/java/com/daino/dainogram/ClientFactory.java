package com.daino.dainogram;


import static org.drinkless.td.libcore.telegram.TdApi.*;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.nio.file.Path;
import java.nio.file.Paths;

import it.tdlight.client.APIToken;
import it.tdlight.client.AuthenticationData;
import it.tdlight.client.GenericUpdateHandler;
import it.tdlight.client.Result;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.TDLibSettings;
import it.tdlight.common.Init;
import it.tdlight.common.utils.CantLoadLibrary;

public class ClientFactory {
    private static SimpleTelegramClient telegramClient;

    public static SimpleTelegramClient buildClient(Integer apiId, String apiHash, Context applicationContext) throws CantLoadLibrary {
        Init.start();

        // Obtain the API token
        APIToken apiToken = new APIToken(apiId, apiHash);

        // Configure the client
        TDLibSettings settings = TDLibSettings.create(apiToken);
        settings.setDatabaseDirectoryPath(Paths.get(String.valueOf(applicationContext.getFilesDir()).concat("/database")));
        settings.setDownloadedFilesDirectoryPath(Paths.get(String.valueOf(applicationContext.getFilesDir()).concat("/download")));

        telegramClient = new SimpleTelegramClient(settings);

        // Configure the authentication info
        AuthenticationData authenticationData = AuthenticationData.consoleLogin();

        // Add an example update handler that prints when the bot is started
        telegramClient.addUpdateHandler(UpdateAuthorizationState.class, new GenericUpdateHandler<UpdateAuthorizationState>() {
            @Override
            public void onUpdate(UpdateAuthorizationState update) {
                printStatus(update.authorizationState);
            }
        });

        telegramClient.addUpdateHandler(UpdateNewMessage.class, update -> {
            // Get the message content
            MessageContent messageContent = update.message.content;

            // Get the message text
            String text;
            if (messageContent instanceof MessageText) {
                // Get the text of the text message
                text = ((MessageText) messageContent).text.text;
            } else {
                // We handle only text messages, the other messages will be printed as their type
                text = "(" + messageContent.getClass().getSimpleName() + ")";
            }

            // Get the chat title
            telegramClient.send(new GetChat(update.message.chatId), (Result<Chat> chatIdResult) -> {
                // Get the chat response
                Chat chat = chatIdResult.get();
                // Get the chat name
                String chatName = chat.title;

                // Print the message
                Log.d("ClientFactory","Received new message from chat " + chatName + ": " + text);
            });

        });

        // Start the client
        telegramClient.start(authenticationData);

        return telegramClient;
    }

    private static void printStatus(AuthorizationState authorizationState) {
        if (authorizationState instanceof AuthorizationStateReady) {
            Log.d("ClientFactory", "Logged in");
        } else if (authorizationState instanceof AuthorizationStateClosing) {
            Log.d("ClientFactory", "Closing...");
        } else if (authorizationState instanceof AuthorizationStateClosed) {
            Log.d("ClientFactory", "Closed");
        } else if (authorizationState instanceof AuthorizationStateLoggingOut) {
            Log.d("ClientFactory", "Logging out...");
        }
    }
}
