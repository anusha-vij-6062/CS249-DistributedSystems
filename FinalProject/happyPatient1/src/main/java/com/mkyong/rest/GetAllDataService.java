package com.mkyong.rest;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/getAll")
public class GetAllDataService {
    private static final Logger LOG = LoggerFactory.getLogger(GetAllDataService.class);

    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {

        String output = "GET Request with Param : " + msg;

        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        session.execute("USE test001");
        //sr.createKeyspace("newKeyspace1130", "SimpleStrategy", 1);

        //ResultSet result = session.execute("INSERT INTO PatInfo(firstname,lastname,patid,gender,age) VALUES('Jeff','Thomas',1207,'Male',25);");

        ResultSet result2 = session.execute("SELECT * FROM BasicInfo");

        String toprint = "";

        while(!result2.isExhausted()){
            Row ri = result2.one();
            toprint = toprint+ri.getString("patientname")+"</br>";
        }

        connector.close();

        return Response.status(200).entity(toprint).build();

    }
}