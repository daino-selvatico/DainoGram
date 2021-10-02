package it.tdlight.client;

import it.tdlight.common.ExceptionHandler;
import it.tdlight.common.TelegramClient;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitCode;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitPassword;
import org.drinkless.td.libcore.telegram.TdApi.CheckAuthenticationCode;
import org.drinkless.td.libcore.telegram.TdApi.CheckAuthenticationPassword;
import org.drinkless.td.libcore.telegram.TdApi.Error;
import org.drinkless.td.libcore.telegram.TdApi.Function;
import org.drinkless.td.libcore.telegram.TdApi.UpdateAuthorizationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AuthorizationStateWaitPasswordHandler implements GenericUpdateHandler<UpdateAuthorizationState> {

	private final TelegramClient client;
	private final ClientInteraction clientInteraction;
	private final ExceptionHandler exceptionHandler;

	public AuthorizationStateWaitPasswordHandler(TelegramClient client,
			ClientInteraction clientInteraction,
			ExceptionHandler exceptionHandler) {
		this.client = client;
		this.clientInteraction = clientInteraction;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public void onUpdate(UpdateAuthorizationState update) {
		if (update.authorizationState.getConstructor() == AuthorizationStateWaitPassword.CONSTRUCTOR) {
			AuthorizationStateWaitPassword authorizationState =
					(AuthorizationStateWaitPassword) update.authorizationState;
			ParameterInfo parameterInfo = new ParameterInfoPasswordHint(authorizationState.passwordHint,
					authorizationState.hasRecoveryEmailAddress,
					authorizationState.recoveryEmailAddressPattern
			);
			String password = clientInteraction.onParameterRequest(InputParameter.ASK_PASSWORD, parameterInfo);
			Function response = new CheckAuthenticationPassword(password);
			client.send(response, ok -> {
				if (ok.getConstructor() == Error.CONSTRUCTOR) {
					throw new TelegramError((Error) ok);
				}
			}, exceptionHandler);
		}
	}
}
