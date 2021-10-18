package it.unimore.fum.iot.services;

import io.dropwizard.Configuration;
import it.unimore.fum.iot.persistence.DeviceManager;

public class AppConfig extends Configuration {

    private DeviceManager deviceManager = null;

    public DeviceManager getDeviceManager(){

        if(this.deviceManager == null)
            this.deviceManager = new DeviceManager();

        return this.deviceManager;
    }
}