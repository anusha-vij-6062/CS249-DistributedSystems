/**
 * CS249 - Group #2
 * Class definition for Patient Objects that stores all information of a specific patient
 */
package com.cs249.group2;

import org.json.JSONObject;

public class PatientBasicInfo {
    private int id;
    private int patientID;
    private String patientName;
    private String doB;
    private String lastVisted;
    private String address;
    private String gender;
    private int phoneNumber;
    private String createdDate;
    private String status;
    private String symptom;
    private String diagnosis;
    private String treatment;

    /**
     * Takes the request JSON string and puts its field into patient basic info's fields.
     * @param request String representation of the JSON (or array of JSON) object
     *  ['PatientID','PatientName','DoB','Address','Gender','PhoneNumber','CreatedDate','LastVisited','Status','Symptom','Diagnosis','Treatment']
     * @param id patient id
     */
    public PatientBasicInfo(String request,int id) {
        JSONObject jsonRequest = new JSONObject(request);
        this.patientID=id;
        this.id=this.patientID;
        this.patientName=jsonRequest.getString("Patient Name");
        this.address=jsonRequest.getString("Address");
        this.createdDate=jsonRequest.getString("Created Date");
        this.gender=jsonRequest.getString("Gender");
        this.lastVisted=this.createdDate;
        this.doB=jsonRequest.getString("DOB");
        this.status=jsonRequest.getString("Status");
        this.phoneNumber=jsonRequest.getInt("Phone Number");
        this.symptom=jsonRequest.getString("Symptom");
        this.diagnosis = jsonRequest.getString("Diagnosis");
        this.treatment = jsonRequest.getString("Treatment");
    }

    public int getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoB() {
        return doB;
    }

    public String getLastVisted() {
        return lastVisted;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public String getSymptom() { return symptom; }

    public String getDiagnosis() { return diagnosis;}

    public String getTreatment(){ return treatment;}
}
