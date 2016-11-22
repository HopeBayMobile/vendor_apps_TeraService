package com.hopebaytech.teraservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hopebaytech.teraservice.service.TeraApiServer;

/**
 * @author Vince
 *      Created by Vince on 2016/9/5.
 */
public class TeraReceiver extends BroadcastReceiver {
    public static final String CREATE_THUMBNAIL_ACTION = "com.teraservice.create.thumbnail";
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent teraAPIServer = new Intent(context, TeraApiServer.class);
            context.startService(teraAPIServer);
        } else if (action.equals(CREATE_THUMBNAIL_ACTION)) {
            long id = intent.getLongExtra("id", -1);
            int type = intent.getIntExtra("type", 0);
            if ( id >= 0 ) {
                Intent teraAPIServer  = new Intent(context, TeraApiServer.class);
                teraAPIServer.setAction(CREATE_THUMBNAIL_ACTION);
                teraAPIServer.putExtra("id", id);
                teraAPIServer.putExtra("type", type);
                context.startService(teraAPIServer);
            }
        }
    }
}
