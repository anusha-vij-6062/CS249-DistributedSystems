package com.mkyong.rest;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class Services {
    private static final Logger LOG = LoggerFactory.getLogger(Services.class);
    @GET
    @Path("/AllBasicInfo")
    public Response getMsg() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        session.execute("USE dataStore");
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