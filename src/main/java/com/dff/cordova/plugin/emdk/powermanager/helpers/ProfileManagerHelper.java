package com.dff.cordova.plugin.emdk.powermanager.helpers;

import android.util.Xml;

import com.dff.cordova.plugin.emdk.powermanager.json.JsonEMDKResult;
import com.dff.cordova.plugin.emdk.powermanager.log.Log;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProfileManagerHelper {
    private static final String TAG = "ProfileManagerHelper";
    
    private ProfileManager profileManager;
    private JsonEMDKResult jsonEMDKResult;
    private Log log;
    
    // Contains the parm-error name (sub-feature that has error)
    private String errorName = "";
    
    // Contains the characteristic-error type (Root feature that has error)
    private String errorType = "";
    
    // contains the error description for parm or characteristic error.
    private String errorDescription = "";
    
    @Inject
    public ProfileManagerHelper(JsonEMDKResult jsonEMDKResult, Log log) {
        this.jsonEMDKResult = jsonEMDKResult;
        this.log = log;
    }
    
    public ProfileManager getProfileManager() {
        return profileManager;
    }
    
    public void setProfileManager(ProfileManager profileManager) {
        log.d(TAG, "profileManager = " + profileManager);
        this.profileManager = profileManager;
    }
    
    public JSONObject processProfile(String profileName, ProfileManager.PROFILE_FLAG set, String[] modifyData) throws JSONException {
        if(profileManager == null) {
            log.e(TAG, "profileManager is null");
            return null;
        }
        EMDKResults results = profileManager.processProfile(profileName, set, modifyData);
    
        JSONObject jsonResult = jsonEMDKResult.toJson(results);
    
        log.i(TAG, "EMDKResult: " + jsonResult);
    
        if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
            // Method call to handle EMDKResult
            handleEMDKResult(results);
        } else {
            log.e(TAG, "Failed to apply profile... "
                + profileName);
        }
        
        return jsonResult;
    }
    
    // Method to handle EMDKResult by extracting response and parsing it
    public void handleEMDKResult(EMDKResults results) {
        // Get XML response as a String
        String statusXMLResponse = results.getStatusString();
        
        try {
            // Create instance of XML Pull Parser to parse the response
            XmlPullParser parser = Xml.newPullParser();
            // Provide the string response to the String Reader that reads
            // for the parser
            parser.setInput(new StringReader(statusXMLResponse));
            // Call method to parse the response
            parseXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            log.e(TAG, e.toString());
        }
        
        // Method call to display results in a dialog
        log.e(TAG, "Name: " + errorName + "; Type: "
            + errorType + "; desc: " + errorDescription);
    }
    
    // Method to parse the XML response using XML Pull Parser
    public void parseXML(XmlPullParser myParser) {
        int event;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        // Get Status, error name and description in case of
                        // parm-error
                        if (name.equals("parm-error")) {
                            errorName = myParser.getAttributeValue(null, "name");
                            errorDescription = myParser.getAttributeValue(null,
                                                                          "desc");
                            
                            // Get Status, error type and description in case of
                            // parm-error
                        } else if (name.equals("characteristic-error")) {
                            errorType = myParser.getAttributeValue(null, "type");
                            errorDescription = myParser.getAttributeValue(null,
                                                                          "desc");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            log.e(TAG, e.getMessage(), e);
        }
    }
}
