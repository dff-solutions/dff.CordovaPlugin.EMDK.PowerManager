package com.dff.cordova.plugin.emdk.powermanager.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.MessageQueue;

import com.dff.cordova.plugin.emdk.powermanager.actions.PluginAction;
import com.dff.cordova.plugin.emdk.powermanager.configurations.ActionHandler;
import com.dff.cordova.plugin.emdk.powermanager.dagger.DaggerManager;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ActionHandlerScope;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;

import javax.inject.Inject;

@ActionHandlerScope
public class ActionHandlerService extends Service implements MessageQueue.IdleHandler {
    private static final String TAG = "ActionHandlerService";
    private boolean mStarted = false;
    private boolean mBound = false;
    
    @Inject
    ActionHandler mActionHandler;
    
    @Inject
    ActionHandlerServiceBinder mBinder;
    
    @Inject
    Log mLog;
    
    @Inject
    @ApplicationContext
    Context mContext;
    
    @Override
    public void onCreate() {
        DaggerManager
            .getInstance()
            .in(getApplication())
            .inject(this);
        
        mLog.d(TAG, "onCreate");
        
        mBinder.setService(this);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActionHandler.getLooper().getQueue().addIdleHandler(this);
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        mLog.d(TAG, "onBind " + intent);
        
        if (!mStarted) {
            throw new IllegalStateException(TAG + " has to be started before binding");
        }
        
        mBound = true;
        
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        mLog.d(TAG, "onUnbind " + intent);
        mBound = false;
        
        return super.onUnbind(intent);
    }
    
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        mLog.d(TAG, "onStartCommand " + intent + " " + flags + " " + startId);
        mStarted = true;
        
        return START_NOT_STICKY;
    }
    
    @Override
    public void onDestroy() {
        mLog.d(TAG, "onDestroy");
        mStarted = false;
        mActionHandler.getLooper().quitSafely();
        mActionHandler = null;
    }
    
    public boolean execute(PluginAction action) {
        return mActionHandler.post(action);
    }
    
    @Override
    public boolean queueIdle() {
        mLog.d(TAG, "queueIdle");
        
        // stop self if there are no more components bound and queue is idle
        if (!mBound) {
            mLog.d(TAG, "no bindings and queue is idle => stopSelf()");
            stopSelf();
        }
        
        return true;
    }
}
