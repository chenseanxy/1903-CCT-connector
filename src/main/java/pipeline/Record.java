package pipeline;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Record {
    private String eid;
    private long time;
    private int placeId;
    private String address;
    private double longitude;
    private double latitude;

    public Record() {
    }

    public Record(String eid, long time, int placeId, String address, double longitude, double latitude) {
        this.eid = eid;
        this.time = time;
        this.placeId = placeId;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    private static Gson gson = new Gson();

    public static Record fromJson(String json){
        return gson.fromJson(json, Record.class);
    }

    public String toJson(){
        return gson.toJson(this);
    }

    public Boolean isValid(){
        if(longitude > 130 || latitude > 40){
            return false;
        }
        return true;
    }

    public static Boolean jsonValid(String json){
        try{
            Record r = fromJson(json);
            Boolean valid = r.isValid();
            if(valid){
                return true;
            } else {
                System.out.println("INVALID " + json);
                return false;
            }
        } catch (JsonSyntaxException e){
            System.out.println("ERROR   " + json);
            return false;
        }
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}


