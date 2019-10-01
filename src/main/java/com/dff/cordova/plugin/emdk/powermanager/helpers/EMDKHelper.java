package com.dff.cordova.plugin.emdk.powermanager.helpers;

import android.content.Context;

import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.emdk.powermanager.json.JsonEMDKResult;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.json.JSONException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EMDKHelper implements EMDKManager.EMDKListener {
    private static final String TAG = "EMDKHelper";
    
    private EMDKManager emdkManager;
    private Log log;
    private ProfileManagerHelper profileManagerHelper;
    private JsonEMDKResult jsonEMDKResult;
    private Context context;
    
    // Assign the profile name used in EMDKConfig.xml
    private String profileName = "PowerManagerProfile";
    
    @Inject
    public EMDKHelper(Log log, ProfileManagerHelper profileManagerHelper,
                      JsonEMDKResult jsonEMDKResult, @ApplicationContext Context context) {
        this.log = log;
        this.profileManagerHelper = profileManagerHelper;
        this.jsonEMDKResult = jsonEMDKResult;
        this.context = context;
    }
    
    public EMDKResults open() {
        // The EMDKManager object will be created and returned in the callback.
        EMDKResults emdkResults = EMDKManager
            .getEMDKManager(context, this);
        
        log.i(TAG, emdkResults.statusCode.name()
            + " "
            + emdkResults.extendedStatusCode.name()
            + " "
            + emdkResults.getExtendedStatusMessage()
        );
        
        return emdkResults;
    }
    
    @Override
    public void onOpened(EMDKManager emdkManager) {
        log.d(TAG, "onOpenend");
        this.emdkManager = emdkManager;
    
        // Get the ProfileManager object to process the profiles
        profileManagerHelper.setProfileManager((ProfileManager) emdkManager
            .getInstance(EMDKManager.FEATURE_TYPE.PROFILE));
    
        if (profileManagerHelper.getProfileManager() != null) {
            String[] modifyData = new String[1];
        
            // Call processProfile with profile name and SET flag to create the
            // profile. The modifyData can be null.
            EMDKResults results = profileManagerHelper.getProfileManager().processProfile(profileName,
                                                                ProfileManager.PROFILE_FLAG.SET, modifyData);
        
            try {
                log.i(TAG, "EMDKResult: " + jsonEMDKResult.toJson(results));
            } catch (JSONException e) {
                log.e(TAG, e.getMessage(), e);
            }
        
            if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
                // Method call to handle EMDKResult
                profileManagerHelper.handleEMDKResult(results);
            } else {
                log.e(TAG, "Failed to apply profile... "
                    + profileName);
            }
        }
    }
    
    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
        }
    }
    
}
