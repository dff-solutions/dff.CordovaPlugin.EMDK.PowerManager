package com.dff.cordova.plugin.emdk.powermanager.dagger.modules;

import android.app.Activity;

import com.dff.cordova.plugin.emdk.powermanager.actions.PluginAction;
import com.dff.cordova.plugin.emdk.powermanager.actions.Reboot;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.CordovaActivity;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.PluginComponentScope;

import org.apache.cordova.CordovaInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

@Module
public class PluginModule {
    private CordovaInterface mCordovaInterface;
    
    public PluginModule(CordovaInterface cordovaInterface) {
        mCordovaInterface = cordovaInterface;
    }
    
    @Provides
    @PluginComponentScope
    @CordovaActivity
    Activity provideCordovaActivity() {
        return mCordovaInterface.getActivity();
    }
    
    @Provides
    @PluginComponentScope
    CordovaInterface provideCordovaInterface() {
        return mCordovaInterface;
    }
    
    @Provides
    @PluginComponentScope
    Map<String, Provider<? extends PluginAction>> provideActionProviders(
        Provider<Reboot> rebootProvider
   
    ) {
        HashMap<String, Provider<? extends PluginAction>> actionProviders = new HashMap<>();
        
        actionProviders.put(Reboot.ACTION, rebootProvider);
        
        return actionProviders;
    }
}
