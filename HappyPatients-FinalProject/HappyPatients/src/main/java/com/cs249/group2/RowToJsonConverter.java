package com.cs249.group2;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import org.apache.cassandra.cql3.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import javax.sql.RowSetMetaData;
import java.sql.SQLException;

public class RowToJsonConverter {
    private JSONObject jsonObject;
    private ResultSet resultSet;
    private JSONArray jsonResponse;

    public RowToJsonConverter( ResultSet r) {
        this.jsonObject = new JSONObject();
        this.resultSet = r;
    }

    JSONArray convertToJSON() throws SQLException {
        jsonResponse=new JSONArray();
        while(!resultSet.isExhausted()){
            Row r = resultSet.one();
            ColumnDefinitions columnData = r.getColumnDefinitions();
            for (int i=0;i<columnData.size();i++) {
                jsonObject.put(columnData.getName(i), r.getObject(i));
            }
            jsonResponse.put(new JSONObject(jsonObject.toString()));
        }
        System.out.println("Converted Result Set to JSON"+jsonResponse);
        return jsonResponse;
    }


}
