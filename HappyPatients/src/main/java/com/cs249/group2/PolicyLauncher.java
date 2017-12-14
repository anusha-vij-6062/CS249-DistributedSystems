package com.cs249.group2;

import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Service;

// Updates cache policy. This is called by the Python policy server
@Path("/Policy")
public class PolicyLauncher {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("Policy")
    public Boolean updatePolicy(String policy) throws Exception {
        JSONObject jsonRequest = new JSONObject(policy);
        System.out.println("In Put");
        Services updateCache = new Services();
        return updateCache.updateCache(jsonRequest);
    }
}
