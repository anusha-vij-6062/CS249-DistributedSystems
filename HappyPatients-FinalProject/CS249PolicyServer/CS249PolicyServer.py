from flask import Flask
import json
from flask import request
import requests

app = Flask(__name__)

Policy = {"Policy Type": "Status", "Policy Value": "ICU"}


@app.route('/Policy', methods=['GET'])
def retrievePolicy():
    return json.dumps(Policy)

@app.route('/Policy', methods=['POST'])
def updatePolicy():
    data = json.loads(request.data)
    print data.keys()
    try:
        if 'Policy Type' in data.keys():
            print "Updating Policy Type from",Policy["Policy Type"], " to ", data["Policy Type"]
            Policy['Policy Type'] = data['Policy Type']

        if 'Policy Value' in data.keys():
            print "Updating Policy Value from"
            print "Updating Policy Value from", Policy["Policy Value"], " to ", data["Policy Value"]
            Policy['Policy Value'] = data['Policy Value']

        ## Make a PUT REQUEST TO UPDATE POLICY
        r = requests.put("http://localhost:8080/Policy/Policy", data=json.dumps(Policy))
        print r.status_code
        if(r.status_code==200):
            return "SUCCESS"
        else:
            return "ERROR:",r.content
    except Exception, err:
            print Exception, err
            return "ERROR"

if __name__ == '__main__':
    app.run()
