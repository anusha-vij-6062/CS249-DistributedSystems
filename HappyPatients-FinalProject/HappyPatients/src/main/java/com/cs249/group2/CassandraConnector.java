/**
 * CS249 - Group #2
 * CassandraConnector based on tutorial at:
 * http://www.baeldung.com/cassandra-with-java
 */
package com.cs249.group2;
import com.datastax.driver.core.*;
import org.json.JSONArray;

import java.sql.SQLException;

public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public CassandraConnector() {
        this.connect("127.0.0.1", null);
        session.execute("USE hpkeyspace");
    }

    private void connect(String node, Integer port) {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();
    }
    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }

    /**
     * Returns the result of the query as an array of JSON.
     * @param query
     * @return array of json where field name is that in the database (colume title)
     * @throws SQLException
     */
    JSONArray queryFromDB(String query) throws SQLException {
        ResultSet result = session.execute(query);
        System.out.println("Result From Database In Result Sets\n" + result);
        RowToJsonConverter r = new RowToJsonConverter(result);
        return r.convertToJSON();
    }
}