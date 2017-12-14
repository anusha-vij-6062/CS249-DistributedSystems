# CS249 Final Project Happy Patients

###Prerequisites

###Synopsis

###Running the Program
1. Make sure Cassandra is started as service and listening on default port.<br>
2. Make sure ActiveMQ is started as service and listening on port 61616 (with UI on 8161).<br>
3. Start Memcache and make sure it is on default port: 11211.<br>
4. Run the Python script CS249PolicyServer.py to start the policy server (will need Flask library, do "pip install flask" if needed). Now the policy server will be listening on port 5000.<br>
5. Start the consumer threads by running Main in the hpMessageConsumers project. <br>
6. Ensure the Cassandra database has the appropriate key space and table created. See input section for details on initializing the database with sample data from a CSV file.<br>  
7. Start the HappyPatients project by running Main. This will start all services by calling their constructors. The HappyPatients app will be listening on port 8080.<br>

###Input

###Output



