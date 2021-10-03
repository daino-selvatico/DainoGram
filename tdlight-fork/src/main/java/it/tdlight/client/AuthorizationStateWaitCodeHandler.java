package it.tdlight.client;

import it.tdlight.common.ExceptionHandler;
import it.tdlight.common.TelegramClient;
import org.drinkless.td.libcore.telegram.TdApi;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitCode;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitOtherDeviceConfirmation;
import org.drinkless.td.libcore.telegram.TdApi.CheckAuthenticationCode;
import org.drinkless.td.libcore.telegram.TdApi.Error;
import org.drinkless.td.libcore.telegram.TdApi.Function;
import org.drinkless.td.libcore.telegram.TdApi.UpdateAuthorizationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AuthorizationStateWaitCodeHandler implements GenericUpdateHandler<UpdateAuthorizationState> {

    private final TelegramClient client;
    private final ClientInteraction clientInteraction;
    private final ExceptionHandler exceptionHandler;

    public AuthorizationStateWaitCodeHandler(TelegramClient client,
                                             ClientInteraction clientInteraction,
                                             ExceptionHandler exceptionHandler) {
        this.client = client;
        this.clientInteraction = clientInteraction;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onUpdate(UpdateAuthorizationState update) {
        //Verification Code Handler
        if (update.authorizationState.getConstructor() == AuthorizationStateWaitCode.CONSTRUCTOR) {
            AuthorizationStateWaitCode authorizationState =
                    (AuthorizationStateWaitCode) update.authorizationState;
            ParameterInfo parameterInfo = new ParameterInfoCode(authorizationState.codeInfo.phoneNumber,
                    authorizationState.codeInfo.nextType,
                    authorizationState.codeInfo.timeout,
                    authorizationState.codeInfo.type
            );
            String code = clientInteraction.onParameterRequest(InputParameter.ASK_CODE, parameterInfo);
            Function response = new CheckAuthenticationCode(code);
            client.send(response, ok -> {
                if (ok.getConstructor() == Error.CONSTRUCTOR) {
                    throw new TelegramError((Error) ok);
                }
            }, exceptionHandler);
        }
    }
}
