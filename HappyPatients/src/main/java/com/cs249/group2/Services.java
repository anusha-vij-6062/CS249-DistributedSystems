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


    public Services() {
        this.cacheConnector = new MemcacheConnector();
        this.policy =new Policy();
    }

    Response selectAllBasicInfo() throws IOException {
        CassandraConnector client = new CassandraConnector();
        //client.connect("127.0.0.1", null);
        Session session = client.getSession();
        //session.execute("USE dataStore");
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

    Response patientBasicInfo(String request) throws Exception {
        CassandraConnector client = new CassandraConnector();
        //client.connect("127.0.0.1", null);
        Session session = client.getSession();
        //session.execute("USE dataStore");
        try {
            JSONObject jsonRequest = new JSONObject(request);
            System.out.println(jsonRequest);
            String query = "SELECT * FROM BasicInfo WHERE patientname = \'" +
                    jsonRequest.getString("Patient Name") + "\' allow filtering";
            System.out.println("Query: " + query);
            ResultSet result = session.execute(query);
            System.out.println(result);
            RowToJsonConverter r = new RowToJsonConverter(result);
            JSONArray response = r.convertToJSON();
            client.close();
            return Response.status(200).entity(response.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            return Response.status(404).entity("404 Not Found").build();
        }
    }

    Response filterPatientBasicInfo(JSONObject request) throws IOException, SQLException {
        JSONArray response = new JSONArray();
        CassandraConnector client = new CassandraConnector();
        //client.connect("127.0.0.1", null);
        //Session session = client.getSession();
        //session.execute("USE dataStore");
        String query;
        ResultSet result;
        RowToJsonConverter r;
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
                        String cacheResponse = this.cacheConnector.getFromCache("ICU");
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
            }
            return Response.status(200).entity(response.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            return Response.status(404).entity("404 Not Found").build();
        }
    }


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
                    "patientname,phonenumber,status) VALUES (";
            query = query+newRecord.getPatientID()+","+newRecord.getPatientID()+",'"+newRecord.getAddress()+"','"+
                    newRecord.getCreatedDate()+"','"+newRecord.getDoB()+"','"+newRecord.getGender()+"','"+
                    newRecord.getLastVisted()+"','"+newRecord.getPatientName()+"',"+newRecord.getPhoneNumber()
                    +",'"+newRecord.getStatus()+"')";
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

    public Response deletePatient(int patientID){
        CassandraConnector client = new CassandraConnector();
        //client.connect("127.0.0.1", null);
        Session session = client.getSession();
        //session.execute("USE dataStore");
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
                client.close();
                updateCache("Deleted");
                return Response.status(200).entity("Patient Record Deleted!").build();
            }
            client.close();
            return Response.status(405).entity("Unable to delete patient Record").build();
        } catch(Exception e)
        {
            e.printStackTrace();
            client.close();
            return Response.status(405).entity("Unable to delete patient Record").build();
        }
    }

    public String queryForCache(JSONObject request) throws Exception  {
        CassandraConnector client = new CassandraConnector();
        String query = "SELECT * FROM BasicInfo WHERE "+request.getString("Policy Type")+" = \'" +
                request.getString("Policy Value") + "\' allow filtering";
        System.out.println("Query: " + query);
        return client.queryFromDB(query).toString();
    }

    public Boolean updateCache(String type) throws Exception {
        //Updating the same policy
        if(type=="Deleted")
        System.out.println("Updating The same cache after a deleted Record");
        else
            System.out.println("Updating Cache after a newly added record which matches cache policy");
        JSONObject currentPolicy = new JSONObject(policy.getCachePolicy().toString());
        return updateCache(currentPolicy);
    }

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



}
