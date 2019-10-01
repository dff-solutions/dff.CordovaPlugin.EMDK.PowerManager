package com.dff.cordova.plugin.emdk.powermanager.actions;

import com.dff.cordova.plugin.emdk.powermanager.EMDKPowerManagerPlugin;
import com.dff.cordova.plugin.emdk.powermanager.helpers.PermissionHelper;
import com.dff.cordova.plugin.emdk.powermanager.json.JsonThrowable;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

public abstract class PluginAction implements Runnable {
    public static final String TAG = "ZebraAction";
    
    protected String mAction;
    private JSONArray mArgs;
    protected CallbackContext mCallbackContext;
    protected JSONObject mJsonArgs = null;
    
    protected boolean mNeedsArgs = false;
    protected boolean mRequiresPermissions = false;
    
    @Inject
    protected Log log;
    
    @Inject
    protected JsonThrowable mJsonThrowable;
    
    @Inject
    protected PermissionHelper mPermissionHelper;
    
    public boolean isNeedsArgs() {
        return mNeedsArgs;
    }
    
    public boolean isRequiresPermissions() {
        return mRequiresPermissions;
    }
    
    public String getAction() {
        return mAction;
    }
    
    public PluginAction setAction(String action) {
        mAction = action;
        
        return this;
    }
    
    public JSONArray getArgs() {
        return mArgs;
    }
    
    public PluginAction setArgs(JSONArray args) {
        mArgs = args;
        
        return this;
    }
    
    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }
    
    public PluginAction setCallbackContext(CallbackContext callbackContext) {
        mCallbackContext = callbackContext;
        
        return this;
    }
    
    
    /**
     * Log a message which action is running and handle exceptions in general.
     */
    @Override
    public final void run() {
        log.i(TAG, String.format("running action: %s(%s)", mAction, getActionName()));
        log.d(TAG, String.format("running action args: %s", getArgs()));
        
        try {
            if (mRequiresPermissions && !hasPermissions()) {
                throw new IllegalStateException(String.format(
                    "required permissions %s not granted",
                    Arrays.toString(EMDKPowerManagerPlugin.PERMISSIONS)
                ));
            }
            
            if (mNeedsArgs) {
                // expect object with arguments as first element in args
                if (mArgs != null) {
                    mJsonArgs = mArgs.getJSONObject(0);
                }
                
                if (mJsonArgs == null) {
                    throw new IllegalArgumentException("args missing");
                }
            }
            
            execute();
        }
        catch (Exception e) {
            log.e(TAG, e.getMessage(), e);
            
            try {
                mCallbackContext.error(mJsonThrowable.toJson(e));
            } catch (JSONException e1) {
                log.e(TAG, e.getMessage(), e);
                mCallbackContext.error(e.getMessage());
            }
        }
    }
    
    /**
     * Called by run.
     *
     * Subclass have to override this method and do their real work here.
     *
     * @throws Exception When actions execution has an error.
     */
    protected abstract void execute() throws Exception;
    
    public abstract String getActionName();
    
    /**
     * Check if required args are included in args.
     *
     * @param requiredArgs Names of required args
     */
    protected void checkJsonArgs(String[] requiredArgs) {
        if (requiredArgs != null) {
            for (String arg: requiredArgs) {
                if (!mJsonArgs.has(arg)) {
                    throw new IllegalArgumentException(String.format("missing arg %s", arg));
                }
            }
        }
    }
    
    protected boolean hasPermissions() {
        return mPermissionHelper.hasAllPermissions(EMDKPowerManagerPlugin.PERMISSIONS);
    }
}