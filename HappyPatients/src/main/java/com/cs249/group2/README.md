README<br>
## Download the following packages in python virtual env
    1. pip install json
    2. pip install configparser
    3. pip install requests
    4. pip install pandas
    5. pip install pyspark
    6. pip install statsmodels
    7. pip install sklearn

## To run the DataGeneration
	1. Navigate to the smartCommute directory
	2. cd PredictionEngine/DataGeneration
	3. Edit the config.ini file to add more streets
	4. python simulate.py
#### Import script to import data into MFS or MongoDB
    1. python ImportData.py
    If importing data to MapR-FS
        i. Enter the source path 
        ii. Enter the destination path 
    If importing data to MongoDB
        i. Enter only the source path 
        ii. The file will get imported to dataStore Database
            under trafficDataSet collection

## Backend Server
    1. cd PredictionEngine/Server
    2. import FLASK_APP = server.py
    3. flask run -h 0.0.0.0
            
## Preprocessing 
    Ensure your flask server is running.
    1. cd PredictionEngine/Preprocess
    2. python Automation.py
    3. Ensure that Cache.json has the output
   