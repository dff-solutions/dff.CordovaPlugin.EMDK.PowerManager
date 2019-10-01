package com.dff.cordova.plugin.emdk.powermanager.actions;

import android.util.Xml;

import com.dff.cordova.plugin.emdk.powermanager.helpers.ProfileManagerHelper;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

import javax.inject.Inject;

public class Reboot extends PluginAction {
    public static final String ACTION = "reboot";
    private String profileName = "PowerManagerProfile";
    private ProfileManagerHelper profileManager;
    
    @Inject
    public Reboot(ProfileManagerHelper profileManager){
        this.profileManager = profileManager;
    }
    
    @Override
    protected void execute() throws Exception {
        String[] modifyData = new String[1];
        modifyData[0] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<characteristic type=\"Profile\">"
            + "<parm name=\"ProfileName\" value=\"PowerManagerProfile\"/>"
            + "<characteristic type=\"PowerMgr\">"
            + "<parm name=\"ResetAction\" value=\"" + 4 + "\"/>"
            + "</characteristic>"
            + "</characteristic>";
    
        JSONObject jsonResult = profileManager.processProfile(profileName,
                                                            ProfileManager.PROFILE_FLAG.SET, modifyData);
        if(jsonResult == null) {
            mCallbackContext.error("result is null");
        } else {
            mCallbackContext.success(jsonResult);
        }
        
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
