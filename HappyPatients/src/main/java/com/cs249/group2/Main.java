package com.cs249.group2;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.SQLException;

public class Main
{
    int jersyPortNumber;
    int memCachePortNumber;
    String cacheServer;
    String policyServer;
    int policyPortNumber;
    MemcacheConnector cacheConnector;
    Policy policy;

    public Main(int jerseyPortNumber,int memCachePortNumber,String cacheServer,String policyServer,int policyPortNumber)
            throws Exception {
        this.jersyPortNumber=jerseyPortNumber;
        this.memCachePortNumber=memCachePortNumber;
        this.cacheServer=cacheServer;
        this.policyServer=policyServer;
        this.policyPortNumber=policyPortNumber;
        System.out.println("Starting Cache");
        initCache();
        System.out.println("Starting Backend Services");
        initServices();
    }

    private void initCache() throws Exception {
        //Make a connection to memcache server
        this.cacheConnector = new MemcacheConnector(this.memCachePortNumber,this.cacheServer);
        cacheConnector.connect();
        if(cacheConnector.test()){
            System.out.println("Memcached connection test passed");
        }
        else{
            System.out.println("Check the connection and Port Number to Memcached Server.");
        }
        //This makes a get call to the policy server and initialized the POLICY class object to be used by all Modules
        //{"Policy Type": "Status", "Policy Value": "ICU"}
        //1. Make a GET Call to POLICY SERVER

        String url="http://"+policyServer+":"+policyPortNumber+"/Policy";
        RESTserviceClient restGET= new RESTserviceClient(url);
        restGET.restClientGET();
        if(restGET.getResponseCode() == 200 ) {
            JSONObject policyJson = new JSONObject(restGET.getResponse().toString());
            System.out.println("Current Policy is: "+policyJson);
            this.policy = new Policy(policyJson);
            System.out.println("Policy Read");
            Services initialCacheAdd = new Services();
            String cacheQueryValue =initialCacheAdd.queryForCache(policyJson);
            cacheConnector.addToCache(policyJson.getString("Policy Value"),cacheQueryValue);
            System.out.println("Added the key "+policyJson.getString("Policy Value")+ " To Cache!");
        }
        else {
            System.out.println("Unable to Initialize Cache");
            System.exit(-1);
        }
    }

    private void initServices(){
        ResourceConfig config = new ResourceConfig();
        config.packages("com.cs249.group2");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(this.jersyPortNumber);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            server.destroy();
        }
    }

    public static void main( String[] args ) throws Exception {
        Main m = new Main(8080,11211,"127.0.0.1","localhost",5000);
        /*
        Type of User: Admin
                      Start New Key Space
                      New Table

        Type of User: Hospital
                      Insert
                      Delete
                      Update
                      Query
         */
    }
}
