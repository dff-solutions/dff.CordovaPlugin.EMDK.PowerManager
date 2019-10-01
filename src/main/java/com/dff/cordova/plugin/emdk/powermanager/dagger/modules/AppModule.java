package com.dff.cordova.plugin.emdk.powermanager.dagger.modules;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.PatternMatcher;

import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ActionHandlerServiceIntent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.emdk.powermanager.dagger.components.PluginComponent;
import com.dff.cordova.plugin.emdk.powermanager.services.ActionHandlerService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {
    ActionHandlerServiceComponent.class,
    PluginComponent.class
})
public class AppModule {
    private Context mApplicationContext;
    
    public AppModule(
        Context applicationContext) {
        mApplicationContext = applicationContext;
    }
    
    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplicationContext;
    }
    
    @Provides
    BluetoothAdapter provideBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }
    
    @Provides
    @Singleton
    @ActionHandlerServiceIntent
    Intent provideActionHandlerServiceIntent(@ApplicationContext Context context) {
        return new Intent(context, ActionHandlerService.class);
    }
    
    @Provides
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
