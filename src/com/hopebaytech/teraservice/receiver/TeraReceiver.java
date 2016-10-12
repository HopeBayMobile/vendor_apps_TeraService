package com.hopebaytech.teraservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hopebaytech.teraservice.service.TeraApiServer;

/**
 * @author Vince
 *      Created by nana on 2016/9/5.
 */
public class TeraReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent teraAPIServer = new Intent(context, TeraApiServer.class);
            context.startService(teraAPIServer);
        }
    }
}
