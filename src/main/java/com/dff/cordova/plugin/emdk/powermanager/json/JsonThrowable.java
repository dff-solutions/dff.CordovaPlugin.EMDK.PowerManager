package com.dff.cordova.plugin.emdk.powermanager.json;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonThrowable {
    private JsonStacktrace mJsonStacktrace;
    
    @Inject
    public JsonThrowable(JsonStacktrace jsonStacktrace) {
        mJsonStacktrace = jsonStacktrace;
    }
    
    public JSONObject toJson(Throwable e) throws JSONException {
        JSONObject jsonThrowable = new JSONObject();
        jsonThrowable.put("className", e.getClass().getName());
        jsonThrowable.put("message", e.getMessage());
        jsonThrowable.put("stackTrace", mJsonStacktrace.toJson(e.getStackTrace()));
        
        Throwable cause = e.getCause();
        
        if (cause != null) {
            jsonThrowable.put("cause", toJson(cause));
        }
        
        return jsonThrowable;
    }
}
