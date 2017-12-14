# CS249 Final Project Happy Patients

# Prerequisites
- Apache ActiveMQ
- Cassandra 3.9.0
- Memcache
- java version "1.8.0_144"
- Python 2.7

# Synopsis
<ul>
<li>Happy Patients is an application that allows users to manage and view patient data.
Users can add, delete, update, and retrieve patient information in this application.
<li>The application also uses Memcache to help improve retrieval speed so that not all requests requires a Cassandra query.
<li> A Policy Server maintains cache policies and users can retrieve and update the cache policies based on the need.
<li>Message consumers listens on a message queue (in ActiveMQ) for messages published by the Happy Patients app and runs appropriate routines when messages are received.
</ul>

# Running the Program
1. Make sure Cassandra is started as service and listening on default port.<br>
2. Make sure ActiveMQ is started as service and listening on port 61616 (with UI on 8161).<br>
3. Start Memcache and make sure it is on default port: 11211.<br>
4. Run the Python script CS249PolicyServer.py to start the policy server (will need Flask library, do "pip install flask" if needed). Now the policy server will be listening on port 5000.<br>
5. Start the consumer threads by running Main in the hpMessageConsumers project. <br>
6. Ensure the Cassandra database has the appropriate key space and table created. See input section for details on initializing the database with sample data from a CSV file.<br>  
7. Start the HappyPatients project by running Main. This will start all services by calling their constructors. The HappyPatients app will be listening on port 8080.<br>

# Input
To initialize the Cassandra database for this project. Create a keyspace 'test001' and a table 'BasicInfo" with the following commands in sql shell:
```
CREATE KEYSPACE test001 WITH REPLICATION = {'class' : 'SimpleStrategy','replication_factor' : 3};
```

```
create table BasicInfo(id int,PatientID int,PatientName text,DoB timestamp,Address text,Gender text,PhoneNumber int,CreatedDate timestamp,LastVisited timestamp, Status text, primary key(PatientID));
```

```
COPY BasicInfo (id,PatientID,PatientName,DoB,Address,Gender,PhoneNumber,CreatedDate,LastVisited,Status) FROM 'BasicInfo.csv' with HEADER = TRUE;
```

Once the database is initialized, patient records can be retrieved, edited, deleted, and added. See the PowerPoint slides for the endpoints and the appropriate request bodies and expected response.

# Output

See the PowerPoint presentation for the expected responses for different requests.


