package it.tdlight.client;

import it.tdlight.common.ExceptionHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.common.utils.ScannerUtils;
import org.drinkless.td.libcore.telegram.TdApi;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitEncryptionKey;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitPhoneNumber;
import org.drinkless.td.libcore.telegram.TdApi.CheckDatabaseEncryptionKey;
import org.drinkless.td.libcore.telegram.TdApi.Error;
import org.drinkless.td.libcore.telegram.TdApi.PhoneNumberAuthenticationSettings;
import org.drinkless.td.libcore.telegram.TdApi.SetAuthenticationPhoneNumber;
import org.drinkless.td.libcore.telegram.TdApi.UpdateAuthorizationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AuthorizationStateWaitAuthenticationDataHandler implements GenericUpdateHandler<UpdateAuthorizationState> {

    private final TelegramClient client;
    private final Authenticable authenticable;
    private final ExceptionHandler exceptionHandler;

    public AuthorizationStateWaitAuthenticationDataHandler(TelegramClient client,
                                                           Authenticable authenticable,
                                                           ExceptionHandler exceptionHandler) {
        this.client = client;
        this.authenticable = authenticable;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onUpdate(UpdateAuthorizationState update) {
        if (update.authorizationState.getConstructor() == TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR) {
            AuthenticationData authenticationData = authenticable.getAuthenticationData();

            // Ask login parameters
            if (authenticationData instanceof ConsoleInteractiveAuthenticationData) {
                ((ConsoleInteractiveAuthenticationData) authenticationData).askData();
            }

            if (authenticationData.isBot()) {
                String botToken = authenticationData.getBotToken();
                TdApi.CheckAuthenticationBotToken response = new TdApi.CheckAuthenticationBotToken(botToken);
                client.send(response, ok -> {
                    if (ok.getConstructor() == Error.CONSTRUCTOR) {
                        throw new TelegramError((Error) ok);
                    }
                }, exceptionHandler);
            } else if (authenticationData.isQrCode()) {
                TdApi.RequestQrCodeAuthentication response = new TdApi.RequestQrCodeAuthentication();
                client.send(response, ok -> {
                    if (ok.getConstructor() == Error.CONSTRUCTOR) {
                        throw new TelegramError((Error) ok);
                    }
                }, exceptionHandler);
            } else {
                PhoneNumberAuthenticationSettings phoneSettings = new PhoneNumberAuthenticationSettings(false, false, false);

                String phoneNumber = String.valueOf(authenticationData.getUserPhoneNumber());
                SetAuthenticationPhoneNumber response = new SetAuthenticationPhoneNumber(phoneNumber, phoneSettings);
                client.send(response, ok -> {
                    if (ok.getConstructor() == Error.CONSTRUCTOR) {
                        throw new TelegramError((Error) ok);
                    }
                }, exceptionHandler);
            }
        }
    }
}
