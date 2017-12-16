package com.cs249.group2;
import java.io.IOException;
import java.net.InetSocketAddress;
import net.spy.memcached.*;

public class MemcacheConnector {
    private static int memCachePort;
    private static String serverAddress;
    private static MemcachedClient mcc;

    public MemcacheConnector(int memCachePort, String serverAddress) {
        this.memCachePort = memCachePort;
        this.serverAddress = serverAddress;
    }

    public MemcacheConnector() {
    }

    public void connect(){
        try {
            mcc = new MemcachedClient(new
                    InetSocketAddress(serverAddress, memCachePort));
            System.out.println("Connection to server sucessfully");
        } catch (IOException e) {
            System.out.println("Connection to server Unsucessful");
            e.printStackTrace();
        }
    }

    public String getFromCache(String key) {
        System.out.println("Query for key"+key);
        System.out.println(mcc.get(key));
        if(mcc.get(key)==null)
            return "-1";
        else
            return mcc.get(key).toString();
    }

    public boolean addToCache(String key, String value){
        System.out.println("Adding to key"+key);
        mcc.add(key,900,value).getStatus();
        if(mcc.get(key).equals(value)){
            System.out.println("Successfully Added Key!");
            return true;
        }
        else
            return false;
    }

    //Memcached set command is used to set a new value to a new or existing key.
   public boolean setCache(String key,String value){
        System.out.println(mcc.set(key,900,value).getStatus()); //900 is expireation time,k
        if(mcc.get(key).equals(value)){
            System.out.println("Successfully Set Key!");
            return true;
        }
        else return false;
    }

    public boolean replaceCache(String key,String value){
        String oldValue = mcc.get(key).toString();
        System.out.println(mcc.replace(key,800,value).getStatus());
        System.out.println(mcc.get(key));
        if(mcc.get(key).equals(oldValue)){
            System.out.println("Failed to replace key!");
            return false;
        }
        else
            System.out.println("Successfully replaced key!");
            return true;
    }

    public boolean deleteFromCache(String key){
        System.out.println(mcc.delete(key).getStatus());
        return false;
    }

    public boolean appendDataToKey(String key,String data) {
        System.out.println(mcc.get(key));
        if (mcc.get(key) != null) {
            System.out.println(mcc.append(key, data).getStatus());
            return true;
        }
        else
            System.out.println("No Key found!");
        return false;

    }


    public boolean test() {
        System.out.println("set status:"+mcc.add("tutorialspoint", 900, "memcached"));
        System.out.println("Get from Cache:"+mcc.get("tutorialspoint"));
        if(mcc.get("tutorialspoint").equals("memcached"))
            return true;
        else
            return false;

    }

}
