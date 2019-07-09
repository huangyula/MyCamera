package com.hiscene.flytech.event;


import com.hiscene.flytech.lifecycle.LifeCycleComponentManager;

import org.greenrobot.eventbus.EventBus;

public class EventCenter {

    private static final EventBus instance = new EventBus();


    public static EventBus getInstance() {
        return instance;
    }

    public static SimpleEventHandler bindContainerAndHandler( Object container, SimpleEventHandler handler) {
        LifeCycleComponentManager.tryAddComponentToContainer(handler, container);
        return handler;
    }
}