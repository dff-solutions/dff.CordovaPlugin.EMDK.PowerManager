package com.dff.cordova.plugin.emdk.powermanager.exceptions;

import com.dff.cordova.plugin.emdk.powermanager.log.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UnexpectedExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "UnexpectedExceptionHandler";
    
    @Inject
    Log mLog;
    
    @Inject
    public UnexpectedExceptionHandler(Log log) {
        mLog = log;
    }
    
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        mLog.e(TAG, "UncaughtException in " + thread + " Exception = " + throwable, throwable);
    }
}
