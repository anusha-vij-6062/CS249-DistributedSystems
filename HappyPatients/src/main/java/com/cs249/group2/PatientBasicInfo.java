package com.cs249.group2;
/*
['PatientID','PatientName','DoB','Address','Gender','PhoneNumber','CreatedDate','LastVisited','Status']
 */

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

    public PatientBasicInfo(int patientID, String patientName, String doB, String lastVisted, String address, String gender, int phoneNumber, String createdDate, String status) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.doB = doB;
        this.lastVisted = lastVisted;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.status = status;
        this.id=this.patientID;
    }

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
}
