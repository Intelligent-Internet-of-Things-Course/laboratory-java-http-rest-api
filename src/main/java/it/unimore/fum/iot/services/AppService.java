package it.unimore.fum.iot.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import it.unimore.fum.iot.model.DeviceDescriptor;
import it.unimore.fum.iot.resources.DeviceResource;

public class AppService extends Application<AppConfig> {

    public static void main(String[] args) throws Exception{
        new AppService().run(new String[]{"server", args.length > 0 ? args[0] : "configuration.yml"});
    }

    public void run(AppConfig appConfig, Environment environment) throws Exception {

        addDemoDevice(appConfig);

        //Add our defined resources
        environment.jersey().register(new DeviceResource(appConfig));

    }

    private void addDemoDevice(AppConfig appConfig){

        DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
        deviceDescriptor.setUuid("device00001");
        deviceDescriptor.setManufacturer("Acme-Inc");
        deviceDescriptor.setSoftwareVersion("v0.0.0.1");
        deviceDescriptor.setType("iot:demosensor");

        appConfig.getDeviceManager().createNewDevice(deviceDescriptor);
    }
}