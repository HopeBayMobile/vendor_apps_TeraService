package com.hopebaytech.teraservice.info;

/**
 * @author Vince
 *      Created by Vince on 2016/9/5.
 */
public class TeraIntent {

    // Intent action 
    public static final String ACTION_UPLOAD_COMPLETED = "hbt.intent.action.UPLOAD_COMPLETED";
    public static final String ACTION_TOKEN_EXPIRED = "hbt.intent.action.TOKEN_EXPIRED";
    public static final String ACTION_RESTORE_STAGE_1 = "hbt.intent.action.RESTORE_STAGE_1";
    public static final String ACTION_RESTORE_STAGE_2 = "hbt.intent.action.RESTORE_STAGE_2";
    public static final String ACTION_EXCEED_PIN_MAX = "hbt.intent.action.EXCEED_PIN_MAX";
    public static final String ACTION_BOOSTER_PROCESS_COMPLETED = "hbt.intent.action.BOOSTER_PROCESS_COMPLETED";
    public static final String ACTION_BOOSTER_PROCESS_FAILED = "hbt.intent.action.BOOSTER_PROCESS_FAILED";

    // Intent 
    public static final String KEY_RESTORE_ERROR_CODE = "intent_key_restore_error_code";

}
