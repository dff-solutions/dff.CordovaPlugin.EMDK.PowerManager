package com.dff.cordova.plugin.emdk.powermanager.dagger.components;

import com.dff.cordova.plugin.emdk.powermanager.EMDKPowerManagerPlugin;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.emdk.powermanager.dagger.modules.PluginModule;

import dagger.Subcomponent;

@PluginComponentScope
@Subcomponent(modules = { PluginModule.class })
public interface PluginComponent {
    @Subcomponent.Builder
    interface Builder {
        Builder pluginModule(PluginModule module);
        PluginComponent build();
    }
    
    void inject(EMDKPowerManagerPlugin plugin);
}
