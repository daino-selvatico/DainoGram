package it.tdlight.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

import it.tdlight.common.TelegramClient;
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateReady;
import org.drinkless.td.libcore.telegram.TdApi.Error;
import org.drinkless.td.libcore.telegram.TdApi.GetMe;
import org.drinkless.td.libcore.telegram.TdApi.UpdateAuthorizationState;
import org.drinkless.td.libcore.telegram.TdApi.User;

final class AuthorizationStateReadyGetMe implements GenericUpdateHandler<UpdateAuthorizationState> {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationStateReadyGetMe.class);

	private final TelegramClient client;
	private final AtomicReference<User> me;

	public AuthorizationStateReadyGetMe(TelegramClient client, AtomicReference<User> me) {
		this.client = client;
		this.me = me;
	}

	@Override
	public void onUpdate(UpdateAuthorizationState update) {
		if (update.authorizationState.getConstructor() == AuthorizationStateReady.CONSTRUCTOR) {
			client.send(new GetMe(), me -> {
				if (me.getConstructor() == Error.CONSTRUCTOR) {
					throw new TelegramError((Error) me);
				}
				this.me.set((User) me);
			}, error -> logger.warn("Failed to execute TdApi.GetMe()"));
		}
	}
}
