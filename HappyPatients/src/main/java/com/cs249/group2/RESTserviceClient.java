package com.cs249.group2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RESTserviceClient {
    private URL url;
    private String postBody;
    private String putBody;
    private int responseCode;
    private StringBuffer response;

    public RESTserviceClient(String urlResource) throws IOException {
        this.url = new URL(urlResource);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public StringBuffer getResponse() {
        return response;
    }

    public String restClientGET(){
        try{
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return "ERROR: Connection to Policy Server Unsuccessful";
        }
    }
}
