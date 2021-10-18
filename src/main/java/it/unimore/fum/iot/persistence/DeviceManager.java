package it.unimore.fum.iot.persistence;

import it.unimore.fum.iot.model.DeviceDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project laboratory-http-api
 * @created 12/10/2021 - 21:08
 */
public class DeviceManager {

    private HashMap<String, DeviceDescriptor> deviceMap;

    public DeviceManager() {
        this.deviceMap = new HashMap<>();
    }

    public List<DeviceDescriptor> getDeviceList() {
        return new ArrayList<>(this.deviceMap.values());
    }

    public DeviceDescriptor getDevice(String deviceId){
        return this.deviceMap.get(deviceId);
    }

    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor){

        //TODO Can be improved generating an Exception
        if(deviceDescriptor == null || this.getDevice(deviceDescriptor.getUuid()) != null)
           return null;

        this.deviceMap.put(deviceDescriptor.getUuid(), deviceDescriptor);
        return deviceDescriptor;
    }

    public DeviceDescriptor updateDevice(DeviceDescriptor deviceDescriptor){
        this.deviceMap.put(deviceDescriptor.getUuid(), deviceDescriptor);
        return deviceDescriptor;
    }

    public DeviceDescriptor deleteDevice(String deviceId){
        return this.deviceMap.remove(deviceId);
    }

}
