package com.cs249.group2;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import net.spy.memcached.MemcachedClient;
import org.apache.cassandra.cql3.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.sql.RowSetMetaData;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Services {
    MemcacheConnector cacheConnector;
    Policy policy;
    CassandraConnector client;


    public Services() {
        client = new CassandraConnector();
        this.cacheConnector = new MemcacheConnector();
        this.policy =new Policy();
    }

    /**
     * Helper method for endpoint /Crew/AllRecords
     * @return formated table with all the patient's records
     */
    Response selectAllBasicInfo() throws IOException, SQLException {
        CassandraConnector client = new CassandraConnector();
        Session session = client.getSession();
        ResultSet result = session.execute("SELECT * FROM BasicInfo");
        System.out.println(result.toString());
        String body = "";
        String row = "";
        String tab = "\t";
        String response = "";
        String heading = "<H2>Patient Information Table</H2>" +
                "<body><table><col width=\"80\">\n" +
                "  <col width=\"150\"><tr>" +
                "  <col width=\"150\"><tr>" +
                "  <col width=\"180\"><tr>" +
                "  <col width=\"180\"><tr>" +
                "  <col width=\"180\"><tr>" +
                "  <col width=\"180\"><tr>" +
                " <th> PatientID </th>" +
                "<th>Patient Name </th>" +
                "<th>Address </th>"
                + "<th>Gender</th>" +
                "<th>Status</th>" +
                "<th>Last Visited</th>" +
                "<th>Date Of Birth</th> </tr>";
        try {
            while (!result.isExhausted()) {
                Row ri = result.one();
                int patientID = ri.getInt("patientid");
                row = "<tr><td>" + patientID + "</td>";
                String patientName = ri.getString("patientname");
                row = row + "<td>" + patientName + "</td>";
                String address = ri.getString("address");
                row = row + "<td>" + address + "</td>";
                String gender = ri.getString("gender");
                row = row + "<td>" + gender + "</td>";
                String status = ri.getString("status");
                row = row + "<td>" + status + "</td>";
                Date lastVisit = ri.getTimestamp("lastvisited");
                row = row + "<td>" + lastVisit + "</td>";
                Date dob = ri.getTimestamp("dob");
                row = row + "<td>" + dob + "</td></tr>";
                System.out.println(row);
                body = body + row;
            }
            response = heading + body + "</table></body>";
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            return Response.status(404).entity("404 Not Found").build();
        }
        client.close();
        return Response.status(200).entity(response).build();
    }

    /**
     * Used to retrieve info regarding one patient. Checks the Cache first
     * to see if the patient is in the cache. Queries Cassandra if not.
     * @param request JSON String of format {"Patient Name":"Alan Caver"}
     * @return response object of type "javax.ws.rs.core.Response;"
     * @throws Exception
     */
    Response patientBasicInfo(String request) throws Exception {

        JSONObject jsonRequest = new JSONObject(request);
        System.out.println(jsonRequest);
        String patName = jsonRequest.getString("Patient Name");
        System.out.println(patName);

        //check cache first to see if patient there
        JSONObject patRecordFromCache = lookupJsonArrByName(patName,getAllFromCache());
        if(patRecordFromCache!=null){
            System.out.println("Cache hit!");
            return Response.status(200).entity(patRecordFromCache.toString()).build();
        }
        System.out.println("Cache miss! Query Cassandra!");


        CassandraConnector client = new CassandraConnector();
        try {
            String query = "SELECT * FROM BasicInfo WHERE patientname = \'" +
                    jsonRequest.getString("Patient Name") + "\' allow filtering";
            System.out.println("Query: " + query);
            JSONArray response=client.queryFromDB(query);
            client.close();
            return Response.status(200).entity(response.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            return Response.status(404).entity("404 Not Found").build();
        }
    }

    /**
     * Helper method that returns everything in Cache
     * @return content/value of cache as a JSONArray
     * returns an empty JSONArray if not found.
     */
    private JSONArray getAllFromCache(){

        JSONArray response = new JSONArray();
        String cacheResponse = this.cacheConnector.getFromCache(policy.
                getCachePolicy("Policy Value"));

        if(cacheResponse !="-1"){
            System.out.println("Sucessfully go cache content!");
            System.out.println(cacheResponse);
            response = new JSONArray(cacheResponse); //constructs a json array from string
            System.out.println("JSON Result Queried from Cache");
        }
        //If the policy asks to query from cache but it is  not present, then add to cache.
        else if (cacheResponse =="-1") {
            System.out.println("Failed to retrieve cache content");
        }

        return response;
    }

    /**
     * Helper method that checks the JSONArray to see if it has a JSONObject with name field = patName
     * returns null otherwise.
     * @param patName name of patient we'd like info for
     * @param resultsArr array of JSON objects each of which represents a patient's record
     * @return JSON object with info for the patient we're interested in.
     */
    private JSONObject lookupJsonArrByName(String patName, JSONArray resultsArr){
        for(Object jObj: resultsArr){
            JSONObject result = (JSONObject) jObj;
            if(result.getString("patientname").compareToIgnoreCase(patName)==0){
                return result;
            }
        }
        System.out.println("Patient not in cache");

        return null;

    }

    /**
     * Filters the patient info table based on differet fields and valeus
     * @param request of the form {"Filter Type": "Gender", "Filter Value": "Male"}
     */
    Response filterPatientBasicInfo(JSONObject request) throws IOException, SQLException {
        JSONArray response = new JSONArray();
        CassandraConnector client = new CassandraConnector();
        String query;
        try {
            String filter = request.getString("Filter Type");
            switch (filter) {
                case "Gender":
                    //If the Policy is set to gender, then query from cache else normal query and response
                    query = "SELECT * FROM BasicInfo WHERE gender = \'" + request.getString("Filter Value") +
                            "\' " + "allow filtering";
                    System.out.println("Query: " + query);
                    if(policy.getCachePolicy("Policy Type").equals("Gender")){
                            String cacheResponse = this.cacheConnector.getFromCache(policy.
                                    getCachePolicy("Policy Value"));
                            if(cacheResponse !="-1"){
                                System.out.println("Cache HIT!");
                                System.out.println(cacheResponse);
                                response = new JSONArray(cacheResponse);
                                System.out.println("Result Queried from Cache");
                                break;
                            }
                            //If the policy asks to query from cache but it is  not present, then add to cache.
                            else if (cacheResponse =="-1") {
                            System.out.println("Cache Miss!");
                            response = client.queryFromDB(query);
                            cacheConnector.addToCache(policy.getCachePolicy("Policy Value").toString(),
                                    response.toString());
                            break;
                        }
                    }
                    else {
                        //When the Cache Policy is not set to GENDER, Query Normally.
                        response = client.queryFromDB(query);
                        break;
                    }

                case "Last Visited Year":
                    query = "SELECT * FROM BasicInfo WHERE lastvisited >= \'" +
                        request.getString("Filter Value") + "\' allow filtering";
                    System.out.println("Query: " + query);
                    if(policy.getCachePolicy("Policy Type").equals(filter)) {
                        //Query from Cache
                        String cacheResponse = this.cacheConnector.getFromCache(policy
                                .getCachePolicy("Policy Value")
                        );
                        if (cacheResponse == "-1") {
                            System.out.println("Cache Miss,Adding to Cache");
                            response = client.queryFromDB(query);
                            cacheConnector.addToCache(policy.getCachePolicy("Policy Value").toString(),
                                    response.toString());
                            break;
                        } else {
                            System.out.println("Cache Hit!");
                            response = new JSONArray(cacheResponse);
                            System.out.println("Result Queried from Cache");
                            break;
                        }
                    }
                    else{
                        //When the Cache Policy is not set to Last Visited Year, Query Normally.
                        response = client.queryFromDB(query);
                        break;
                    }

                case "Status":
                    query = "SELECT * FROM BasicInfo WHERE status = \'" +
                            request.getString("Filter Value") + "\' allow filtering";
                    System.out.println("Query: " + query);
                    if(policy.getCachePolicy("Policy Type").equals(filter)){
                        System.out.println(request.getString("Filter Value"));
                        String cacheResponse = this.cacheConnector.getFromCache("ICU"); //ICU should be replaced with policy.getFromCache("Policy Type")
                        if (cacheResponse != "-1") {
                            System.out.println("Cache HIT!");
                            response = new JSONArray(cacheResponse);
                            System.out.println("Response From Cache");
                            break;
                        } else if (cacheResponse == "-1") {
                            System.out.println("Cache Miss,Adding to Cache");
                            response = client.queryFromDB(query);
                            cacheConnector.addToCache(policy.getCachePolicy("Policy Value").toString(),
                                    response.toString());
                        }
                    }
                    else {
                        response =client.queryFromDB(query);
                        break;
                    }
                case "Diagnosis":
                    query = "SELECT * FROM BasicInfo WHERE diagnosis = \'" +
                            request.getString("Filter Value") + "\' allow filtering";
                    System.out.println("Query: " + query);
                    response =client.queryFromDB(query);
                    break;
                case "Treatment":
                    query = "SELECT * FROM BasicInfo WHERE treatment = \'" +
                            request.getString("Filter Value") + "\' allow filtering";
                    System.out.println("Query: " + query);
                    response =client.queryFromDB(query);
                    break;

            }
            return Response.status(200).entity(response.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            return Response.status(404).entity("404 Not Found").build();
        }
    }

    /**
     * Called when /Crew/NewPatientInfoRecord endpoint is hit
     * rev: added new patient fields, k
     * @param request: String representation of the JSON object in request
     * @return
     * @throws Exception
     */
    Response addPatientInfo(String request) throws Exception {
        CassandraConnector client = new CassandraConnector();
        Session session = client.getSession();
        try {
            String idQuery="SELECT MAX(patientid) FROM BasicInfo";
            ResultSet result=session.execute(idQuery);
            Row r=result.one();
            int lastID=r.getInt("system.max(patientid)");
            PatientBasicInfo newRecord=new PatientBasicInfo(request,lastID+1);
            String query = "INSERT INTO BasicInfo (id,patientid,address,createddate,dob,gender,lastvisited," +
                    "patientname,phonenumber,status,symptom,diagnosis,treatment) VALUES (";
            query = query+newRecord.getPatientID()+","+newRecord.getPatientID()+",'"+newRecord.getAddress()+"','"+
                    newRecord.getCreatedDate()+"','"+newRecord.getDoB()+"','"+newRecord.getGender()+"','"+
                    newRecord.getLastVisted()+"','"+newRecord.getPatientName()+"',"+newRecord.getPhoneNumber()
                    +",'"+newRecord.getStatus()+"','"+newRecord.getSymptom()+"','"+newRecord.getDiagnosis()+"','"+newRecord.getTreatment()+"')";
            System.out.println(query);
            session.execute(query);
            result=session.execute(idQuery);
            r=result.one();
            int newlastID=r.getInt("system.max(patientid)");
            System.out.println(newlastID);
            updateCache("New Record Added");
            if(newlastID>lastID){
                client.close();
                return Response.status(200).entity("Patient Added! Patient ID is "+newlastID).build();
            }
            client.close();
            return Response.status(405).entity("Unable to added New patient Record").build();
        } catch(Exception e)
        {
            e.printStackTrace();
            client.close();
            return Response.status(405).entity("Unable to added New patient Record").build();
        }
    }

    /**
     * Deletes the record of the patient with specified patientID
     * @param patientID id of patient to be removed
     */
    public Response deletePatient(int patientID){
        Session session = client.getSession();
        try{
            String rowCountQuery="select count(*) from BasicInfo";
            ResultSet result = session.execute(rowCountQuery);
            System.out.println(result);
            Row r=result.one();
            System.out.println(r.getObject(0));
            Long rowCountOld =(Long) r.getObject(0);
            String deleteQuery="delete from BasicInfo where patientid ="+patientID;
            session.execute(deleteQuery);
            result = session.execute(rowCountQuery);
            r=result.one();
            Long rowCountNew =(Long) r.getObject(0);
            if(rowCountNew<rowCountOld) {
                updateCache("Deleted");
                return Response.status(200).entity("Patient Record Deleted!").build();
            }
            return Response.status(405).entity("Unable to delete patient Record").build();
        } catch(Exception e)
        {
            e.printStackTrace();
            client.close();
            return Response.status(405).entity("Unable to delete patient Record").build();
        }
    }

    public String queryForCache(JSONObject request) throws Exception  {
        JSONObject mapping = new JSONObject("{\"Last Visited Year\":\"lastvisited\"," +
                "\"Created Date\":\"createddate\",\"DOB\":\"dob\"}");
        System.out.println(mapping);

        if(request.getString("Policy Type").equals("Last Visited Year") ||
                request.getString("Policy Type").equals("Created Date") ||
                request.getString("Policy Type").equals("DoB")) {
            System.out.println("Cache policy is a date");
            String query = "SELECT * FROM BasicInfo WHERE "+mapping.get(request.getString("Policy Type"))+" >= " +
                    request.getString("Policy Value") + " allow filtering";
            System.out.println("Query: " + query);
            return client.queryFromDB(query).toString();
        }
        String query = "SELECT * FROM BasicInfo WHERE "+request.getString("Policy Type")+" = \'" +
                request.getString("Policy Value") + "\' allow filtering";
        System.out.println("Query: " + query);
        return client.queryFromDB(query).toString();
    }

    /* This function is called when some new record is added or deleted and has the cache policy type and value
    //Wrapper function*/
    public Boolean updateCache(String type) throws Exception {
        //Updating the same policy
        System.out.println("Updating Cache after a record is "+type);
        JSONObject currentPolicy = policy.getCachePolicy();
        return updateCache(currentPolicy);
    }

     /*Function that updates cache with parameters JSONObject updatedPolicy
     This function is called when the policy server  makes a PUT call [Which was inititaed by the user making a post call to the POLICY Server ]
     Updates the cache*/
    public Boolean updateCache(JSONObject updatedPolicy) throws Exception {
        //Extract the old key value
        System.out.println("OLD:-\n"+policy.getCachePolicy());
        String oldCacheKey = policy.getCachePolicy("Policy Value");

        //Updating Cache Policy
        policy.setCachePolicy("Policy Type",updatedPolicy.getString("Policy Type"));
        policy.setCachePolicy("Policy Value",updatedPolicy.getString("Policy Value"));
        System.out.println("New:- \n"+policy.getCachePolicy());
        int same = 0;
        if(oldCacheKey.equals(policy.getCachePolicy("Policy Value"))){
            same = 1;
        }
        String newCacheResponse = queryForCache(updatedPolicy);

        // True if they are the same key
        // False if they are different key
        switch (same){
            case 0: //Delete the old one
                System.out.println("Not the same key");
                cacheConnector.deleteFromCache(oldCacheKey);
                return cacheConnector.addToCache(policy.getCachePolicy("Policy Value"),newCacheResponse);
            case 1: // Just Set it
                System.out.println("Same key");
                return cacheConnector.setCache(policy.getCachePolicy("Policy Value"),newCacheResponse);
        }
        return false;
    }

    /**
     * Updates information for an existing patient in Cassandra DB
     * @param updateRequest JSON specifying the new field and values of the patient
     */
    public String updatePatientInfo(JSONObject updateRequest) throws Exception {
        String oldRecord;
        String newRecord;
        int patientID;
        try {
            patientID=updateRequest.getInt("Patient ID");
        }
        catch (Exception e){
            e.printStackTrace();
            String postBody="\"{\n\t\"Patient ID\":23,\n\t\"Patient Name\":\"Ji Klink\",\n\t\"" +
                    "Last Visited\":\"\",\n\t\"Status\":\"Ongoing\",\n\t\"DOB\":" +
                    "\"\",\n\t\"Address\":\"\",\n\t\"Created Date\":\n}\"";
            return "POST BODY Mandatory Field \"Patient ID\"\n"+"Rest Field ID are\n"+postBody;
        }
        System.out.println(updateRequest.keySet());
        updateRequest.keySet();
        String updateQueryHead = "UPDATE BasicInfo SET ";
        String updateQueryTail = " WHERE patientid="+patientID;
        String setBody="";
        int comm=0;

        //update BasicInfo set gender='Female', address = 'nowhwere' where patientid=23;
        for (String temp: updateRequest.keySet()){
            switch (temp){
                case "Status":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                        setBody=setBody+"status=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",status=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;

                case "Patient Name":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"patientname=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",patientname=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;
                case "Address":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"address=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",address=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;

                case "Last Visited":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"lastvisited=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",lastvisited=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;
                case "Created Date":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"createddate=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",createddate=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;

                case "Dob":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"dob=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",dob=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;
                case "Gender":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"gender=\'"+updateRequest.get(temp)+"\'";
                        }
                        else setBody=setBody+",gender=\'"+updateRequest.get(temp)+"\'";
                    }
                    comm++;
                    break;

                case "Symptom":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else{
                        if( comm == 0){
                            setBody=setBody + "symptom=\'"+updateRequest.get(temp)+"\'";
                        }else{
                            setBody=setBody+",symptom=\'"+updateRequest.get(temp)+"\'";
                        }
                    }
                    comm++;
                    break;

                case "Diagnosis":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }

                    else{

                        if( comm == 0){
                            setBody=setBody + "diagnosis=\'"+updateRequest.get(temp)+"\'";
                        }else{
                            setBody=setBody+",diagnosis=\'"+updateRequest.get(temp)+"\'";
                        }
                    }
                    comm++;
                    break;

                case "Treatment":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else{

                        if( comm == 0){
                            setBody=setBody + "treatment=\'"+updateRequest.get(temp)+"\'";
                        }else{
                            setBody=setBody+",treatment=\'"+updateRequest.get(temp)+"\'";
                        }
                    }
                    comm++;
                    break;

                case "Phone Number":
                    if(updateRequest.get(temp).equals("")){
                        break;
                    }
                    else {
                        if (comm==0){
                            setBody=setBody+"phonenumber="+updateRequest.get(temp);
                        }
                        else setBody=setBody+",phonenumber="+updateRequest.get(temp);
                    }
                    comm++;
                    break;
                default:
                    System.out.println("Skipping "+temp);
            }
            System.out.println(updateRequest.get(temp));
        }
        String queryWithPid= "SELECT * FROM BasicInfo where patientid = "+patientID;
        System.out.println(queryWithPid);
        JSONArray response =client.queryFromDB(queryWithPid);
        oldRecord=response.toString();
        String finalQuery=updateQueryHead+setBody+updateQueryTail;
        System.out.println("FINAL QUERY\n"+finalQuery);
        Session session=client.getSession();
        session.execute(finalQuery);
        newRecord = client.queryFromDB(queryWithPid).toString();
        updateCache("Update");
        return "Updated Info from\n"+oldRecord+"To\n"+newRecord;

    }
}
