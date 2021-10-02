package it.tdlight.common;

import org.drinkless.td.libcore.telegram.TdApi.Object;

/**
 * Interface for handler for results of queries to TDLib and incoming updates from TDLib.
 */
public interface ResultHandler {

	/**
	 * Callback called on result of query to TDLib or incoming update from TDLib.
	 *
	 * @param object Result of query or update of type TdApi.Update about new events.
	 */
	void onResult(Object object);
}