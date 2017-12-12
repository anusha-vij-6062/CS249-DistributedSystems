package com.cs249.group2;
import com.datastax.driver.core.Session;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedConnection;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import javax.ws.rs.core.Response;
import javax.xml.ws.Service;


@Path("/Crew")
public class Launcher{
    public Launcher() {
    }

    //Not done: TODO:
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("UpdatePatientRecord")
    public String remoteSlaveLaunchPUT(PatientBasicInfo patientInfo){
        System.out.println("In Put");
        System.out.println(patientInfo);
        return "Hello, world!";
    }

    //Returns records of ALL patients
    @GET
    @Path("BasicInfo")
    public Response allBasicInfo() throws IOException {
        Services selectBasicInfo= new Services();
        return selectBasicInfo.selectAllBasicInfo();
    }

    //Returns info of Patient given patient name is the input
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientInfo")
    public Response patientInfo(String request) throws Exception {
        Services patientBasicInfo= new Services();
        return patientBasicInfo.patientBasicInfo(request);
    }

    /* Filters patients based on Gender, Last visited year and Status
    Cache Incorported in this
    If the policy [static JSONObject cachePolicy] has the policy type of Status,Last visited year or  Gender, then
    the result is returned from Cache*/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientInfoFilter")
    public Response filterPatientInfo(String request) throws Exception {
        Services basicInfoFilter= new Services();
        JSONObject filterRequest = new JSONObject(request);
        return basicInfoFilter.filterPatientBasicInfo(filterRequest);
    }

    //Adds new Patient Record
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("NewPatientInfoRecord")
    public Response newPatientRecord(String request) throws Exception {
        Services updatePatientRecord= new Services();
        return updatePatientRecord.addPatientInfo(request);
    }
    //Deletes patient record
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientInfo/{patientID}")
    public Response deletePatientRecord(@PathParam("patientID") int patientID) throws Exception {
        Services patientBasicInfo= new Services();
        return patientBasicInfo.deletePatient( patientID);
    }
}

