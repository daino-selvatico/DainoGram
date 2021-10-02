package it.tdlight.common;

import org.drinkless.td.libcore.telegram.TdApi.Object;
import java.util.List;

/**
 * Interface for handler for incoming updates from TDLib.
 */
public interface UpdatesHandler {

	/**
	 * Callback called on incoming update from TDLib.
	 *
	 * @param object Updates of type {@link org.drinkless.td.libcore.telegram.TdApi.Update} about new events, or {@link
	 *               org.drinkless.td.libcore.telegram.TdApi.Error}.
	 */
	void onUpdates(List<Object> object);
}