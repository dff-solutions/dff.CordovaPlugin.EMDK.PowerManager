package com.dff.cordova.plugin.emdk.powermanager.dagger.components;

import com.dff.cordova.plugin.emdk.powermanager.dagger.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = {
    AppModule.class
})
public interface AppComponent {
    PluginComponent.Builder pluginComponentBuilder();
    ActionHandlerServiceComponent.Builder actionHandlerServiceComponentBuilder();
}
