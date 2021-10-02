package it.tdlight.client;

import it.tdlight.common.ExceptionHandler;
import it.tdlight.common.TelegramClient;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitEncryptionKey;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitTdlibParameters;
import org.drinkless.td.libcore.telegram.TdApi.CheckDatabaseEncryptionKey;
import org.drinkless.td.libcore.telegram.TdApi.Error;
import org.drinkless.td.libcore.telegram.TdApi.SetTdlibParameters;
import org.drinkless.td.libcore.telegram.TdApi.TdlibParameters;
import org.drinkless.td.libcore.telegram.TdApi.UpdateAuthorizationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AuthorizationStateWaitEncryptionKeyHandler implements GenericUpdateHandler<UpdateAuthorizationState> {

	private final TelegramClient client;
	private final ExceptionHandler exceptionHandler;

	public AuthorizationStateWaitEncryptionKeyHandler(TelegramClient client, ExceptionHandler exceptionHandler) {
		this.client = client;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public void onUpdate(UpdateAuthorizationState update) {
		if (update.authorizationState.getConstructor() == AuthorizationStateWaitEncryptionKey.CONSTRUCTOR) {
			client.send(new CheckDatabaseEncryptionKey(), ok -> {
				if (ok.getConstructor() == Error.CONSTRUCTOR) {
					throw new TelegramError((Error) ok);
				}
			}, exceptionHandler);
		}
	}
}
