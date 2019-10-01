package com.dff.cordova.plugin.emdk.powermanager.log;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

@Singleton
public class CallbackContextAppender extends AppenderBase<ILoggingEvent> {
    private EventBus mEventBus;
    
    @Inject
    public CallbackContextAppender(EventBus eventBus) {
        mEventBus = eventBus;
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }
        
        if (mEventBus.hasSubscriberForEvent(ILoggingEvent.class)) {
            mEventBus.post(event);
        }
    }
}
