package com.dff.cordova.plugin.emdk.powermanager.dagger.modules;

import com.dff.cordova.plugin.emdk.powermanager.configurations.ActionHandler;
import com.dff.cordova.plugin.emdk.powermanager.configurations.ActionHandlerThread;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ActionHandlerScope;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.ActionHandlerServiceComponent;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@Module
public class ActionHandlerServiceModule {
    @Provides
    @ActionHandlerScope
    ActionHandler provideActionHandler(ActionHandlerThread actionHandlerThread) {
        actionHandlerThread.start();
        return new ActionHandler(actionHandlerThread.getLooper());
    }
}
