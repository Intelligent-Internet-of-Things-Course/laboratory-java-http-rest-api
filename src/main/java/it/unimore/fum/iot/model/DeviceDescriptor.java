package it.unimore.fum.iot.model;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project laboratory-http-api
 * @created 12/10/2021 - 21:06
 */
public class DeviceDescriptor {

    private String uuid;

    private String type;

    private String softwareVersion;

    private String manufacturer;

    public DeviceDescriptor() {
    }

    public DeviceDescriptor(String uuid, String type, String softwareVersion, String manufacturer) {
        this.uuid = uuid;
        this.type = type;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IoTDevice{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", softwareVersion='").append(softwareVersion).append('\'');
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
