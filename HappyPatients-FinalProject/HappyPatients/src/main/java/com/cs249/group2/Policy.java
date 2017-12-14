package com.cs249.group2;

import org.json.JSONObject;
public class Policy {
    static JSONObject cachePolicy;

    public Policy(JSONObject cachePolicy) {
        this.cachePolicy = cachePolicy;
    }

    public String getCachePolicy(String key) {
        return cachePolicy.getString(key);
    }

    public Policy() {
    }

    public String setCachePolicy(String key, String value) {
        cachePolicy.put(key, value);
        if (cachePolicy.getString(key).equals(value))
            return "ADDED";
        else
            return "ERROR";
    }

    public static JSONObject getCachePolicy() {
        return cachePolicy;
    }
}
