package com.kingdevelopers.www.vehicledatabase.model;

/**
 * Created by saibaba on 12/13/2016.
 */
public class Vehicle {
    private String carModel="";
    private String Make="";
    private String Number="";
    private int type=1;
    private int sNo=0;
    private int Oid=0;
    private int uId=0;
    public int getOid() {
        return Oid;
    }
    public void setOid(int oid) {
        Oid = oid;
    }
    public int getuId() {
        return uId;
    }
    public void setuId(int uId) {
        this.uId = uId;
    }
    public int getsNo() {
        return sNo;
    }
    public void setsNo(int sNo) {
        this.sNo = sNo;
    }
    public String getNumber() {
        return Number;
    }
    public void setNumber(String number) {
        Number = number;
    }
    public String getMake() {
        return Make;
    }
    public void setMake(String make) {
        Make = make;
    }
    public String getCarModel() {
        return carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    public int getType() {
        return type;
    }
}
