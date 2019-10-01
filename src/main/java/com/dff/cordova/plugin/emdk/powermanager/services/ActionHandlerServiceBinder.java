package com.dff.cordova.plugin.emdk.powermanager.services;

import android.os.Binder;

import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ActionHandlerScope;

import javax.inject.Inject;

@ActionHandlerScope
public class ActionHandlerServiceBinder extends Binder {
    private ActionHandlerService mActionHandlerService;
    
    @Inject
    public ActionHandlerServiceBinder() {}
    
    public ActionHandlerService getService() {
        return mActionHandlerService;
    }
    
    public void setService(ActionHandlerService actionHandlerService) {
        mActionHandlerService = actionHandlerService;
    }
}
