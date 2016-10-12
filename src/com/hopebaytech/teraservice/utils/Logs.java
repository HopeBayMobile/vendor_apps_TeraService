package com.hopebaytech.teraservice.utils;

import android.util.Log;

/**
 * @author Aaron
 *         Created by Aaron on 2016/5/24.
 */
public class Logs {

    public static final String TAG = "TeraService";
    public static final int LOG_LEVEL = Log.DEBUG;

    public static void i(String className, String funcName, String logMsg) {
        log(Log.INFO, className, funcName, logMsg);
    }

    public static void w(String className, String funcName, String logMsg) {
        log(Log.WARN, className, funcName, logMsg);
    }

    public static void e(String className, String funcName, String logMsg) {
        log(Log.ERROR, className, funcName, logMsg);
    }

    public static void d(String className, String funcName, String logMsg) {
        log(Log.DEBUG, className, funcName, logMsg);
    }

    public static void i(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.INFO, className, innerClassName, funcName, logMsg);
    }

    public static void w(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.WARN, className, innerClassName, funcName, logMsg);
    }

    public static void e(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.ERROR, className, innerClassName, funcName, logMsg);
    }

    public static void d(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.DEBUG, className, innerClassName, funcName, logMsg);
    }

    private static void log(int logLevel, String className, String funcName, String logMsg) {
        if (logLevel >= LOG_LEVEL) {
            if (logMsg == null) {
                logMsg = "";
            }
            if (logLevel == Log.DEBUG) {
                Log.d(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.INFO) {
                Log.i(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.WARN) {
                Log.w(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.ERROR) {
                Log.e(TAG, className + "(" + funcName + "): " + logMsg);
            }
        }
    }

    private static void log(int logLevel, String className, String innerClassName, String funcName, String logMsg) {
        if (logLevel >= LOG_LEVEL) {
            if (logMsg == null) {
                logMsg = "";
            }
            if (logLevel == Log.DEBUG) {
                Log.d(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.INFO) {
                Log.i(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.WARN) {
                Log.w(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.ERROR) {
                Log.e(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            }
        }
    }

}

