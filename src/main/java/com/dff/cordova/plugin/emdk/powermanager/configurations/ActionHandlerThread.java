package com.dff.cordova.plugin.emdk.powermanager.configurations;

import android.os.HandlerThread;
import android.os.Process;

import com.dff.cordova.plugin.emdk.powermanager.exceptions.UnexpectedExceptionHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionHandlerThread extends HandlerThread {
    private static final String TAG = "ActionHandlerThread";
    
    @Inject
    public ActionHandlerThread(UnexpectedExceptionHandler unexpectedExceptionHandler) {
        super(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        this.setUncaughtExceptionHandler(unexpectedExceptionHandler);
    }
}
