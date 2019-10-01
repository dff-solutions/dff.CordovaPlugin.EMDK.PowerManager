package com.dff.cordova.plugin.emdk.powermanager;

import android.Manifest;
import android.content.Context;
import android.util.Xml;

import com.dff.cordova.plugin.emdk.powermanager.actions.PluginAction;
import com.dff.cordova.plugin.emdk.powermanager.configurations.ActionsManager;
import com.dff.cordova.plugin.emdk.powermanager.dagger.DaggerManager;
import com.dff.cordova.plugin.emdk.powermanager.helpers.EMDKHelper;
import com.dff.cordova.plugin.emdk.powermanager.helpers.PermissionHelper;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.Arrays;

import javax.inject.Inject;

/**
 * This plugin implements an interface to the PowerManager Android API.
 *
 * @author dff solutions
 */
public class EMDKPowerManagerPlugin extends CordovaPlugin {
    public static final String TAG = "EMDKPowerManagerPlugin";
    public static final int OPTION_DO_NOTHING = 0;
    public static final int OPTION_SLEEP_MODE = 1;
    public static final int OPTION_REBOOT = 4;
    public static final int OPTION_ENTERPRISE_RESET = 5;
    public static final int OPTION_FACTORY_RESET = 6;
    public static final int OPTION_FULL_DDEVICE_WIPE = 7;
    public static final int OPTION_OS_UPDATE = 8;
    public static final int WWAN_DO_NOT_CHANGE = 0;
    public static final int WWAN_ON = 1;
    public static final int WWAN_OFF = 2;
    private Context appContext;
   
    // Initial Value of the Power Manager options to be executed in the
    // onOpened() method when the EMDK is ready. Default Value set in the wizard
    // is 0.
    // 0 -> Do Nothing
    // 1 -> Sleep Mode
    // 4 -> Reboot
    // 5 -> Enterprise Reset
    // 6 -> Factory Reset
    // 7 -> Full Device Wipe
    // 8 -> OS Update
    private int reboot_value = OPTION_DO_NOTHING;
    
    // Initial Value of the wwan is do not change (0)
    // 0 -> Do not change
    // 1 -> Turn on
    // 2 -> Turn off
    private int wwan_value = WWAN_DO_NOT_CHANGE;
    
    private static final int PERMISSION_REQUEST_CODE = 0;
    public static final String[] PERMISSIONS = new String[] {
        Manifest.permission.NFC,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.VIBRATE,
        "com.symbol.emdk.permission.EMDK"
    };
    
    @Inject
    ActionsManager mActionsManager;
    
    @Inject
    PermissionHelper mPermissionHelper;
    
    @Inject
    EMDKHelper emdkHelper;
    
    @Inject
    Log log;
    
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        DaggerManager
            .getInstance()
            .in(cordova.getActivity().getApplicationContext())
            .in(cordova)
            .inject(this);
    }

    /**
     * Called after plugin construction and fields have been initialized. Prefer
     * to use pluginInitialize instead since there is no reboot_value in having
     * parameters on the initialize() function.
     *
     */
    public void pluginInitialize() {
        super.pluginInitialize();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        emdkHelper.open();
    }
    
    /*
    // Method that applies the modified settings to the EMDK Profile based on
    // user selected options of Power Manager feature.
    private void modifyProfile_XMLString(CallbackContext callbackContext, String action) throws JSONException {
        if (profileManager != null) {
            // Prepare XML to modify the existing profile
            String[] modifyData = new String[1];
            if(action.equals("reboot")) {
                // Modified XML input for Sleep and Reboot feature based on user
                // selected options of radio button
                // reboot_value = 1 -> Sleep Mode
                // reboot_value = 4 -> Reboot
                modifyData[0] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<characteristic type=\"Profile\">"
                    + "<parm name=\"ProfileName\" value=\"PowerManagerProfile\"/>"
                    + "<characteristic type=\"PowerMgr\">"
                    + "<parm name=\"ResetAction\" value=\"" + reboot_value + "\"/>"
                    + "</characteristic>"
                    + "</characteristic>";
                modifyData[0] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<characteristic type=\"Profile\">"
                    + "<parm name=\"ProfileName\" value=\"PowerManagerProfile\"/>"
                    + "<characteristic type=\"WirelessMgr\">"
                    + "<parm name=\"WWANState\" value=\"" + 1 + "\"/>"
                    + "</characteristic>"
                    + "</characteristic>";
            } else if(action.equals("wwanTurnOn") || action.equals("wwanTurnOff")) {
                // Modified XML
                // wwan_value = 1 -> Turn on
                // wwan_value = 2 -> Turn off
                modifyData[0] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<characteristic type=\"Profile\">"
                    + "<parm name=\"ProfileName\" value=\"PowerManagerProfile\"/>"
                    + "<characteristic type=\"WirelessMgr\">"
                    + "<parm name=\"WWANState\" value=\"" + wwan_value + "\"/>"
                    + "</characteristic>"
                    + "</characteristic>";
            }
            

            // Call process profile to modify the profile of specified profile
            // name
            EMDKResults results = profileManager.processProfile(profileName,
                    ProfileManager.PROFILE_FLAG.SET, modifyData);

            JSONObject jsonResult = EMDKResultToJson(results);

            log.i(TAG, "EMDKResult: " + EMDKResultToJson(results));

            if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
                // Method call to handle EMDKResult
                handleEMDKResult(results);
            } else {
                log.e(TAG, "Failed to apply profile... "
                        + profileName);
            }

            callbackContext.success(jsonResult);
        } else {
            String msg = "profile manager not instantiated";
            log.e(TAG, msg);
            callbackContext.error(msg);
        }
    }

     */
    /**
     * Executes the request.
     * <p>
     * This method is called from the WebView thread. To do a non-trivial amount
     * of work, use: cordova.getThreadPool().execute(runnable);
     * <p>
     * To run on the UI thread, use:
     * cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return Whether the action was valid.
     */
    @Override
    public boolean execute(String action, JSONArray args,
                           final CallbackContext callbackContext) throws JSONException {
        PluginAction actionInstance;
    
        log.d(TAG, "execute: " + action);
    
        try {
            // check permissions
            if (! requestPermissions()) {
                // try action anyway since not all actions require permission.
                // actions are queued and PERMISSIONS might be granted when action is running
                log.w(TAG, String.format(
                    "required permissions %s not granted",
                    Arrays.toString(PERMISSIONS)
                ));
            }
        
            actionInstance = mActionsManager
                .createAction(action, args, callbackContext);
        
            if (actionInstance != null) {
                boolean result = mActionsManager.runAction(actionInstance);
            
                if (! result) {
                    throw new Exception("could not queue action " + actionInstance.getActionName());
                }
            
                return true;
            }
        
            return super.execute(action, args, callbackContext);
        } catch (Exception e) {
            log.e(TAG, e.getMessage(), e);
            callbackContext.error(e.getMessage());
        
            return false;
        }
    }
    
    /**
     * Requests permissions if user has not selected the Don't ask again option
     * for all permissions.
     *
     * @return True if all permissions are granted false otherwise
     */
    private boolean requestPermissions() {
        boolean allGranted = mPermissionHelper.hasAllPermissions(PERMISSIONS);
        
        log.d(TAG, String.format("all permissions granted: %b", allGranted));
        
        if (!allGranted &&
            mPermissionHelper.shouldShowRequestPermissionRationale(cordova.getActivity(), PERMISSIONS)
        ) {
            log.d(TAG, String.format("request permissions for %s", Arrays.toString(PERMISSIONS)));
            cordova.requestPermissions(this, PERMISSION_REQUEST_CODE, PERMISSIONS);
        }
        
        return allGranted;
    }
}
