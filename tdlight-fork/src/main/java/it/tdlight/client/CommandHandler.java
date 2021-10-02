package it.tdlight.client;

import org.drinkless.td.libcore.telegram.TdApi;
import org.drinkless.td.libcore.telegram.TdApi.Chat;
import org.drinkless.td.libcore.telegram.TdApi.User;

public interface CommandHandler {

	void onCommand(Chat chat, TdApi.MessageSender commandSender, String arguments);
}
