package com.fidelreactlibrary.events;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.fidelreactlibrary.adapters.abstraction.DataProcessor;

public final class BridgeLibraryEventEmitter implements DataProcessor<ReadableMap> {

    private final ReactApplicationContext reactContext;
    private final BridgeLibraryEvent eventToEmit;

    public BridgeLibraryEventEmitter(ReactApplicationContext reactContext, BridgeLibraryEvent eventToEmit) {
        this.reactContext = reactContext;
        this.eventToEmit = eventToEmit;
    }

    @Override
    public void process(ReadableMap data) {
        reactContext
                .getJSModule(RCTDeviceEventEmitter.class)
                .emit(eventToEmit.getEventName(), data);
    }
}
