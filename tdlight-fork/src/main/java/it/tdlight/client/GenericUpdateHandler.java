package it.tdlight.client;

import org.drinkless.td.libcore.telegram.TdApi.Object;
import org.drinkless.td.libcore.telegram.TdApi.Update;

/**
 * Interface for incoming updates from TDLib.
 */
@FunctionalInterface
public interface GenericUpdateHandler<T extends Update> {

	/**
	 * Callback called on incoming update from TDLib.
	 *
	 * @param update Update of type TdApi.Update about new events.
	 */
	void onUpdate(T update);
}