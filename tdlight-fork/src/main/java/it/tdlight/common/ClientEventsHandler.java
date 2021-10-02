package it.tdlight.common;

import org.drinkless.td.libcore.telegram.TdApi.Object;

public interface ClientEventsHandler {

	int getClientId();

	void handleEvents(boolean isClosed, long[] eventIds, Object[] events);
}
