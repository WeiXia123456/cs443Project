package edu.umb.cs443;

public class parkingLot {
    private String name;
    private float longitude;
    private float latitude;
    private String address;
    private int charges;


    public parkingLot(){}

    public parkingLot(String name, float latitude, float longitude, String address, int charges ){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.charges = charges;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float getLatitude() {
        return this.latitude;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public float getLongitude() {
        return this.longitude;
    }
    public void setCharges(int charges){
        this.charges = charges;
    }
    public int getCharges() {
        return this.charges;
    }


}
