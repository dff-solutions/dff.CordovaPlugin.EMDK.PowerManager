package com.dff.cordova.plugin.emdk.powermanager.configurations;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dff.cordova.plugin.emdk.powermanager.actions.PluginAction;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ActionHandlerServiceIntent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;
import com.dff.cordova.plugin.emdk.powermanager.services.ActionHandlerService;
import com.dff.cordova.plugin.emdk.powermanager.services.ActionHandlerServiceBinder;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

@PluginComponentScope
public class ActionsManager implements ServiceConnection {
    private static final String TAG = "ActionsManager";
    
    private Log mLog;
    private ActionHandlerService mActionHandlerService;
    private Map<String, Provider<? extends PluginAction>> mActionProviders;
    private Context mContext;
    private boolean mBound = false;
    
    @Inject
    public ActionsManager(
        @ApplicationContext Context context,
        @ActionHandlerServiceIntent Intent serviceIntent,
        Log log,
        Map<String, Provider<? extends PluginAction>> actionProviders
    ) {
        mContext = context;
        mLog = log;
        mActionProviders = actionProviders;
        
        mContext.startService(serviceIntent);
        mContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }
    
    public PluginAction createAction(
        final String action,
        final JSONArray args,
        final CallbackContext callbackContext
    ) {
        PluginAction actionInstance = null;
        Provider<? extends PluginAction> actionProvider = mActionProviders.get(action);
        
        if (actionProvider != null) {
            actionInstance = actionProvider.get();
        }
        
        if (actionInstance != null) {
            actionInstance
                .setAction(action)
                .setArgs(args)
                .setCallbackContext(callbackContext);
        }
        
        return actionInstance;
    }
    
    public boolean runAction(PluginAction action) {
        if (mBound && mActionHandlerService != null) {
            return mActionHandlerService.execute(action);
        }
        
        throw new IllegalStateException(String.format(
            "%s service %s bound %b",
            TAG,
            mActionHandlerService,
            mBound
        ));
    }
    
    public void onDestroy() {
        mLog.d(TAG, "onDestroy");
        mContext.unbindService(this);
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mLog.d(TAG, "bind service " + name);
        mActionHandlerService = ((ActionHandlerServiceBinder) service).getService();
        mBound = true;
    }
    
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mLog.d(TAG, "unbind service " + name);
        mActionHandlerService = null;
        mBound = false;
    }
    public boolean isBound() {
        return mBound;
    }
}