package com.hopebaytech.teraservice.info;

/**
 * @author Vince
 *          Created by nana on 2016/9/5.
 */
public class HCFSEvent {
    public static final int TEST = 0;
    public static final int TOKEN_EXPIRED = 1;
    public static final int UPLOAD_COMPLETED = 2;
    public static final int RESTORE_STAGE_1 = 3;
    public static final int RESTORE_STAGE_2 = 4;

    // The following event is not implemented.
    public static final int EXCEED_PIN_MAX = 5;
    public static final int SPACE_NOT_ENOUGH = 6;
}
