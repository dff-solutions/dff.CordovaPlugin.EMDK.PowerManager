package com.dff.cordova.plugin.emdk.powermanager.json;

import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonStacktrace {
    @Inject
    public JsonStacktrace() {}
    
    public JSONArray toJson(StackTraceElement[] stackTrace) {
        JSONArray jsonStackTrace = new JSONArray();
        
        if (stackTrace != null) {
            for (StackTraceElement ste : stackTrace) {
                jsonStackTrace.put(ste.toString());
            }
        }
        
        return jsonStackTrace;
        
    }
}
