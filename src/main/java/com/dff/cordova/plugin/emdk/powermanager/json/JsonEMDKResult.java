package com.dff.cordova.plugin.emdk.powermanager.json;

import com.symbol.emdk.EMDKResults;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonEMDKResult {
    
    @Inject
    public JsonEMDKResult(){}
    
    public JSONObject toJson(EMDKResults results) throws JSONException {
        JSONObject jsonResult = new JSONObject();
        String status = "";
        
        switch (results.statusCode) {
            case SUCCESS:
                status = "SUCCESS";
                break;
            case FAILURE:
                status = "FAILURE";
                break;
            case CHECK_XML:
                status = "CHECK_XML";
                break;
            case EMDK_NOT_OPENED:
                status = "EMDK_NOT_OPENED";
                break;
            case EMPTY_PROFILENAME:
                status = "EMPTY_PROFILENAME";
                break;
            case NO_DATA_LISTENER:
                status = "NO_DATA_LISTENER";
                break;
            case NULL_POINTER:
                status = "NULL_POINTER";
                break;
            case PREVIOUS_REQUEST_IN_PROGRESS:
                status = "PREVIOUS_REQUEST_IN_PROGRESS";
                break;
            case PROCESSING:
                status = "PROCESSING";
                break;
            case UNKNOWN:
                status = "UNKNOWN";
                break;
            default:
                status = "UNKNOWN";
                break;
        }
        
        jsonResult.put("status", status);
        jsonResult.put("statusCode", results.statusCode);
        jsonResult.put("statusString", results.getStatusString());
        jsonResult.put("statusDocument", results.getStatusDocument());
        jsonResult.put("extendedStatusMessage", results.getExtendedStatusMessage());
        jsonResult.put("successFeaturesCount", results.getSuccessFeaturesCount());
        jsonResult.put("totalFeaturesCount", results.getTotalFeaturesCount());
        
        return jsonResult;
    }
    
}
