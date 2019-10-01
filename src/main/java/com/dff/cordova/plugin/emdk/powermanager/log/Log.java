package com.dff.cordova.plugin.emdk.powermanager.log;

import android.support.annotation.NonNull;

import com.dff.cordova.plugin.emdk.powermanager.helpers.PackageManagerHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;

@Singleton
public class Log {
    private static final String TAG = "Log";
    
    private final String LOG_FILE_PRIMARY;
    private final String LOG_FILE_HISTORY_PATTERN;
    
    private CallbackContextAppender mCallbackContextAppender;
    private PackageManagerHelper mPackageManagerHelper;
    
    @Inject
    public Log(
        CallbackContextAppender callbackContextAppender,
        PackageManagerHelper packageManagerHelper
    ) {
        mCallbackContextAppender = callbackContextAppender;
        mPackageManagerHelper = packageManagerHelper;
        String logsPath = mPackageManagerHelper.getDataDir();
        
        LOG_FILE_PRIMARY = logsPath + "/plugin-zebra.log";
        LOG_FILE_HISTORY_PATTERN = logsPath + "/plugin-zebra.%i.log";
        
        configureLogbackDirectly();
        
        d(TAG, "logsPath: " + logsPath);
    }
    
    private void configureLogbackDirectly() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
        
        ch.qos.logback.classic.Logger root =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (mPackageManagerHelper.isDebuggable()) {
            root.setLevel(Level.DEBUG);
        }
        else {
            root.setLevel(Level.INFO);
        }
        
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        
        rollingFileAppender.setContext(lc);
        rollingFileAppender.setFile(LOG_FILE_PRIMARY);
        rollingFileAppender.setEncoder(createEncoder(lc));
        rollingFileAppender.setTriggeringPolicy(getTriggeringPolicy());
        rollingFileAppender.setRollingPolicy(getRollingPolicy(lc, rollingFileAppender));
        rollingFileAppender.start();
        
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(lc);
        asyncAppender.addAppender(rollingFileAppender);
        asyncAppender.start();
        root.addAppender(asyncAppender);
        
        PatternLayoutEncoder logcatEncoder = new PatternLayoutEncoder();
        logcatEncoder.setContext(lc);
        logcatEncoder.setPattern("%msg");
        logcatEncoder.start();
        
        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(logcatEncoder);
        logcatAppender.start();
        root.addAppender(logcatAppender);
        
        mCallbackContextAppender.setContext(lc);
        mCallbackContextAppender.start();
        root.addAppender(mCallbackContextAppender);
        
        d(TAG, "configureLogbackDirectly: finished");
    }
    
    @NonNull
    private TriggeringPolicy<ILoggingEvent> getTriggeringPolicy() {
        return new SizeBasedTriggeringPolicy<>("1MB");
    }
    
    @NonNull
    private FixedWindowRollingPolicy getRollingPolicy(LoggerContext lc, RollingFileAppender<ILoggingEvent> rollingFileAppender) {
        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setContext(lc);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setFileNamePattern(LOG_FILE_HISTORY_PATTERN);
        rollingPolicy.setMinIndex(1);
        rollingPolicy.setMaxIndex(1);
        rollingPolicy.start();
        return rollingPolicy;
    }
    
    @NonNull
    private Encoder<ILoggingEvent> createEncoder(LoggerContext lc) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        String LOG_MESSAGE_PATTERN = "%d{ISO8601} [%thread] %-5level %logger - %msg%n";
        encoder.setPattern(LOG_MESSAGE_PATTERN);
        encoder.start();
        return encoder;
    }
    
    private boolean needLog() {
        return true;
    }
    
    public void d(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).debug(msg);
        }
    }
    
    public void d(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).debug(msg, throwable);
        }
    }
    
    public void e(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).error(msg);
        }
    }
    
    public void e(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).error(msg, throwable);
        }
    }
    
    public void i(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).info(msg);
        }
    }
    
    public void i(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).info(msg, throwable);
        }
    }
    
    public void v(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).trace(msg);
        }
    }
    
    public void v(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).trace(msg, throwable);
        }
    }
    
    public void w(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).warn(msg);
        }
    }
    
    public void w(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).warn(msg, throwable);
        }
    }
}

