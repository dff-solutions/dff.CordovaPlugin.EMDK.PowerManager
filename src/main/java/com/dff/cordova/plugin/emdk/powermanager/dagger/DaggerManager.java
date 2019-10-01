package com.dff.cordova.plugin.emdk.powermanager.dagger;

import android.content.Context;

import com.dff.cordova.plugin.emdk.powermanager.EMDKPowerManagerPlugin;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.AppComponent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.DaggerAppComponent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.PluginComponent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.modules.ActionHandlerServiceModule;
import com.dff.cordova.plugin.emdk.powermanager.dagger.modules.AppModule;
import com.dff.cordova.plugin.emdk.powermanager.dagger.modules.PluginModule;
import com.dff.cordova.plugin.emdk.powermanager.services.ActionHandlerService;

import org.apache.cordova.CordovaInterface;

public class DaggerManager {
    private static DaggerManager mDaggerManager;
    
    private AppComponent mAppComponent;
    private PluginComponent mPluginComponent;
    private ActionHandlerServiceComponent mActionHandlerServiceComponent;
    
    private AppModule mAppModule;
    private PluginModule mPluginModule;
    
    private DaggerManager() {}
    
    public static synchronized DaggerManager getInstance() {
        if (mDaggerManager == null) {
            mDaggerManager = new DaggerManager();
        }
        return mDaggerManager;
    }
    
    public DaggerManager in(Context context) {
        if (mAppModule == null && context != null) {
            mAppModule = new AppModule(context);
        }
        
        return this;
    }
    
    public DaggerManager in(CordovaInterface cordovaInterface) {
        if (mPluginModule == null) {
            mPluginModule = new PluginModule(cordovaInterface);
        }
        
        return this;
    }
    
    public void inject(EMDKPowerManagerPlugin plugin) {
        getPluginComponent().inject(plugin);
    }
    
    public void inject(ActionHandlerService actionHandlerService) {
        getActionHandlerServiceComponent()
            .inject(actionHandlerService);
    }
    
    private AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent
                .builder()
                .appModule(mAppModule)
                .build();
        }
        
        return mAppComponent;
    }
    
    private PluginComponent getPluginComponent() {
        if (mPluginComponent == null) {
            mPluginComponent = getAppComponent()
                .pluginComponentBuilder()
                .pluginModule(mPluginModule)
                .build();
        }
        
        return mPluginComponent;
    }
    
    private ActionHandlerServiceComponent getActionHandlerServiceComponent() {
        if (mActionHandlerServiceComponent == null) {
            mActionHandlerServiceComponent = getAppComponent()
                .actionHandlerServiceComponentBuilder()
                .actionHandlerModule(new ActionHandlerServiceModule())
                .build();
        }
        
        return mActionHandlerServiceComponent;
    }
}
