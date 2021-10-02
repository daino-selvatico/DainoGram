package it.tdlight.common;

import org.drinkless.td.libcore.telegram.TdApi.Object;

public interface EventsHandler {

	void handleClientEvents(int clientId, boolean isClosed, long[] clientEventIds, Object[] clientEvents);
}
