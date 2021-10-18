package it.unimore.fum.iot.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import it.unimore.fum.iot.model.DeviceDescriptor;
import it.unimore.fum.iot.services.AppConfig;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/api/iot/inventory/device")
public class DeviceResource {

    private AppConfig conf;

    public DeviceResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevices(@Context ContainerRequestContext req) {

        try {

            System.out.println("Loading all stored IoT Inventory Devices ...");

            List<DeviceDescriptor> deviceList = this.conf.getDeviceManager().getDeviceList();

            if(deviceList == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),
                                "Devices Not Found !"))
                        .build();

            return Response.ok(deviceList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{device_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceWithId(@Context ContainerRequestContext req,
                                    @PathParam("device_id") String deviceId) {

        try {

            System.out.println("Loading devices with id: " + deviceId);

            DeviceDescriptor deviceDescr = this.conf.getDeviceManager().getDevice(deviceId);

            if(deviceDescr == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),
                                "Devices Not Found !"))
                        .build();

            return Response.ok(deviceDescr).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDevice(@Context ContainerRequestContext req,
                                 @Context UriInfo uriInfo,
                                 DeviceDescriptor deviceDescriptor) {

        try {

            System.out.println("Incoming Device Creation Request: " + deviceDescriptor);

            if(deviceDescriptor == null || deviceDescriptor.getUuid() == null || deviceDescriptor.getUuid().length() == 0)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),
                                "Check the request !")).build();

            if(this.conf.getDeviceManager().getDevice(deviceDescriptor.getUuid()) != null)
                return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.CONFLICT.getStatusCode(),
                                "Device with the same UUID already available !")).build();

            this.conf.getDeviceManager().createNewDevice(deviceDescriptor);

            String requestPath = uriInfo.getAbsolutePath().toString();
            String locationHeaderString = String.format("%s/%s",requestPath,deviceDescriptor.getUuid());

            return Response.created(new URI(locationHeaderString)).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDevice(@Context ContainerRequestContext req,
                                 @Context UriInfo uriInfo,
                                 @PathParam("device_id") String deviceId,
                                 DeviceDescriptor deviceDescriptor) {

        try {

            //Check if the request is valid
            if(deviceDescriptor == null || !deviceDescriptor.getUuid().equals(deviceId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Device Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(this.conf.getDeviceManager().getDevice(deviceId) == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device not found !")).build();

            this.conf.getDeviceManager().updateDevice(deviceDescriptor);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDevice(@Context ContainerRequestContext req,
                                 @PathParam("device_id") String deviceId) {

        try {

           //Check the request
            if(deviceId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Device Id Provided !")).build();

            //Check if the device is available or not
            DeviceDescriptor deviceDescriptor = this.conf.getDeviceManager().getDevice(deviceId);
            if(deviceDescriptor == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device Not Found !")).build();

            //Delete the device
            this.conf.getDeviceManager().deleteDevice(deviceId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

}
