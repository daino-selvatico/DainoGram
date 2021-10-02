package com.daino.dainogram;


import android.util.Log;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public class ClientFactory {
    private static Client telegramClient;

    public static Client getInstance() {
        if(telegramClient==null) {
            telegramClient = Client.create(new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.Object object) {
                    Log.i("TELEGRAM CLIENT","onResult ".concat(object.toString()));
                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {

                }
            }, new Client.ExceptionHandler() {
                @Override
                public void onException(Throwable e) {

                }
            });
        }
        return telegramClient;
    }
}
