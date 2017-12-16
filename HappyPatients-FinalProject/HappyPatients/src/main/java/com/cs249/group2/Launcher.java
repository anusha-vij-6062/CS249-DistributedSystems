/**
 * CS249 - Group #2
 * Defines all the endpoints of the HappyPatient App
 */

package com.cs249.group2;
import com.datastax.driver.core.Session;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedConnection;
import org.json.JSONObject;
import org.omg.PortableInterceptor.ACTIVE;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.core.Response;
import javax.xml.ws.Service;


@Path("/Crew")
public class Launcher{
    public Launcher() {
    }

    /**
     * Returns info for all patients in hospital's database.
     */
    @GET
    @Path("AllRecords") //previously "BasicInfo"
    public Response allBasicInfo() throws IOException, SQLException {
        Services selectBasicInfo= new Services();
        return selectBasicInfo.selectAllBasicInfo();
    }

    /**
     * Adds a new patient to the database. The patient id will be automatically assigned as 1 more than the
     * highest patient id currently existing in the system.
     * @param request JSON with fields "Patient Name","Address","Gender","Created Date","DOB","Phone Number",
     * "Status","Symptom","Diagnosis","Treatment". All fields should be included.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientRecord") //previously "NewPatientInfoRecord"
    public Response newPatientRecord(String request) throws Exception {
        Services updatePatientRecord= new Services();
        ActivemqConnector.sendMessageToQueue("HPEmailQueue","new patient added to record");
        ActivemqConnector.sendMessageToQueue("HPAnalyticsQueue","new patient added to record");
        return updatePatientRecord.addPatientInfo(request);
    }

    /**
     * Updates an existing patient's information. Uses the patientid to identify the record to update.
     * @param patientInfo JSON with fields that should be updated (note: all fields except the patientid can be updated)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientRecord") //previously "UpdatePatientRecord"
    public String updatePatientRecord(String patientInfo) throws Exception {
        System.out.println("In put");
        JSONObject updateRequest= new JSONObject(patientInfo);
        Services updateRecord = new Services();

        //Default message to send when updating patient info (other than treatment info "Diagnosis" and "Treatment")
        String msgToSendToQueue = "updated personal information";

        //Check if "Diagnosis" is set to done and send appropriate message
        if(updateRequest.keySet().contains("Diagonsis")){
            if(updateRequest.getString("Diagnosis").compareToIgnoreCase("done")==0){
                msgToSendToQueue = "Diagnosis done.";
            }
        }

        //Check if "Treatment" is set to done and send appropriate message
        if(updateRequest.keySet().contains("Treatment")){
            if(updateRequest.getString("Treatment").compareToIgnoreCase("complete")==0){
                msgToSendToQueue = "Patient treatment completed.";
            } //If treatment is complete, then it is inferred diagnosis is done, so only one msg needs to be sent.
        }

        ActivemqConnector.sendMessageToQueue("HPEmailQueue",msgToSendToQueue);
        ActivemqConnector.sendMessageToQueue("HPAnalyticsQueue",msgToSendToQueue);
        return updateRecord.updatePatientInfo(updateRequest);
    }

    /**
     * Deletes record of the patient whose patientid is equal to the url parameter {patientID}
     * @param patientID Integer value of patientid
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientRecord/{patientID}")  //previously "PatientInfo/{patientID}"
    public Response deletePatientRecord(@PathParam("patientID") int patientID) throws Exception {
        Services patientBasicInfo= new Services();
        ActivemqConnector.sendMessageToQueue("HPEmailQueue","delete personal information");
        ActivemqConnector.sendMessageToQueue("HPAnalyticsQueue","delete personal information");
        return patientBasicInfo.deletePatient( patientID);
    }

    /**
     * Retrieves record of the patient with the specified name.
     * @param request JSON of the format {"Patient Name": "Julie Thomas"}
     * @return a JSON including the value all fields of the patient's record.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("LookupByName") //previously "PatientInfo"
    public Response patientInfo(String request) throws Exception {
        Services patientBasicInfo= new Services();
        return patientBasicInfo.patientBasicInfo(request);
    }

    /**
     * Filters patients based on Gender, Last visited year, Status, Diagnosis, or Treatment
     * This checks the Cache when applicable prior to performing the query in Cassandra
     * If the policy [static JSONObject cachePolicy] has the policy type of Status,Last visited year or  Gender, then
     * the result is returned from Cache
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("PatientInfoFilter")  //endpoint same as before
    public Response filterPatientInfo(String request) throws Exception {
        Services basicInfoFilter= new Services();
        JSONObject filterRequest = new JSONObject(request);
        return basicInfoFilter.filterPatientBasicInfo(filterRequest);
    }

}

