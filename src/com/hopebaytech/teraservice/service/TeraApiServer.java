package com.hopebaytech.teraservice.service;

import android.app.Service;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;

import com.hopebaytech.teraservice.info.HCFSEvent;
import com.hopebaytech.teraservice.info.TeraIntent;
import com.hopebaytech.teraservice.utils.Logs;
import com.hopebaytech.teraservice.utils.ThumbnailApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vince
 *      Created by Vince on 2016/9/5.
 */
public class TeraApiServer extends Service {

    private final String CLASSNAME = getClass().getSimpleName();
    public static String SOCKET_ADDRESS = "mgmt.api.sock";
    private boolean stopped = false;
    private ExecutorService pool = Executors.newFixedThreadPool(5);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logs.d(CLASSNAME, "onStartCommand", null);

        new SocketListener().start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopped = true;
        pool.shutdown();
        super.onDestroy();
    }

    class SocketListener extends Thread {
        @Override
        public void run() {
            Logs.d(CLASSNAME, "run", "Server socket run . . . start");
            LocalServerSocket server = null;
            try {
                server = new LocalServerSocket(SOCKET_ADDRESS);
                while (!stopped) {
                    try {
                        LocalSocket receiver = server.accept();
                        if (receiver != null) {
                            InputStream inputStream = receiver.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                            String inputLine = bufferedReader.readLine();
                            Log.d(CLASSNAME, inputLine);
                            try {
                                JSONArray jsonArray = new JSONArray(inputLine);
                                receiver.getOutputStream().write(1);
                                pool.execute(new Thread(new MgmtApiUtils(jsonArray)));
                            } catch (JSONException e) {
                                Log.e(CLASSNAME, Log.getStackTraceString(e));
                                receiver.getOutputStream().write(0);
                            }

                            bufferedReader.close();
                            receiver.close();
                        }
                    } catch (Exception e) {
                        Logs.e(CLASSNAME, "run", Log.getStackTraceString(e));
                    }
                }
            } catch (IOException e) {
                Log.e(CLASSNAME, Log.getStackTraceString(e));
            } finally {
                if (server != null) {
                    try {
                        server.close();
                    } catch (IOException e) {
                        Log.e(CLASSNAME, Log.getStackTraceString(e));
                    }
                }
            }
            Logs.d(CLASSNAME, "run", "Server socket run . . . end");
        }
    }

    class MgmtApiUtils implements Runnable {
        private JSONArray jsonArray;
        public MgmtApiUtils(JSONArray j) {
            this.jsonArray = j;
        }
        @Override
        public void run() {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    int eventID = jsonObj.getInt("event_id");
                    Logs.w(CLASSNAME, "run", "eventID = " + eventID);
                    switch (eventID) {
                        case HCFSEvent.TEST:
                            Logs.d(CLASSNAME, CLASSNAME + ".test", jsonObj.toString());
                            break;
                        case HCFSEvent.TOKEN_EXPIRED:
                            checkTokenExpiredCause();
                            break;
                        case HCFSEvent.UPLOAD_COMPLETED:
                            sendUploadCompletedIntent();
                            break;
                        case HCFSEvent.RESTORE_STAGE_1:
                            sendRestoreStage1Intent(jsonObj);
                            break;
                        case HCFSEvent.RESTORE_STAGE_2:
                            sendRestoreStage2Intent(jsonObj);
                            break;
                        case HCFSEvent.EXCEED_PIN_MAX:
                            notifyUserExceedPinMax();
                            break;
                        case HCFSEvent.CREATE_THUMBNAIL:
                            new ThumbnailApiUtils().createThumbnailImages(TeraApiServer.this.getApplicationContext(), jsonObj);
                            break;
                        case HCFSevent.BOOSTER_PROCESS_COMPLETED:
                            sendBoosterProcessCompletedIntent();
                            break;
                        case HCFSevent.BOOSTER_PROCESS_FAILED:
                            sendBoosterProcessFailedIntent();
                            break;

                    }
                }
            } catch (Exception e) {
                Logs.e(CLASSNAME, "run", Log.getStackTraceString(e));
            }
        }

        private void notifyUserExceedPinMax() {
            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_EXCEED_PIN_MAX);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void checkTokenExpiredCause() {
            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_TOKEN_EXPIRED);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void sendUploadCompletedIntent() {
            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_UPLOAD_COMPLETED);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void sendRestoreStage1Intent(JSONObject jsonObj) {
            int errorCode = -1;
            try {
                errorCode = jsonObj.getInt("result");
            } catch (JSONException e) {
                Logs.w(CLASSNAME, "sendRestoreStage1Intent", "jsonObj=" + jsonObj.toString());
            }

            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_RESTORE_STAGE_1);
            if (errorCode != -1) {
                intent.putExtra(TeraIntent.KEY_RESTORE_ERROR_CODE, errorCode);
            }
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void sendRestoreStage2Intent(JSONObject jsonObj) {
            int errorCode = -1;
            try {
                errorCode = jsonObj.getInt("result");
            } catch (JSONException e) {
                Logs.w(CLASSNAME, "sendRestoreStage1Intent", "jsonObj=" + jsonObj.toString());
            }

            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_RESTORE_STAGE_2);
            if (errorCode != -1) {
                intent.putExtra(TeraIntent.KEY_RESTORE_ERROR_CODE, errorCode);
            }
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void sendBoosterProcessCompletedIntent() {
            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_BOOSTER_PROCESS_COMPLETED);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

        private void sendBoosterProcessFailedIntent() {
            Intent intent = new Intent();
            intent.setAction(TeraIntent.ACTION_BOOSTER_PROCESS_FAILED);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        }

    }

}
