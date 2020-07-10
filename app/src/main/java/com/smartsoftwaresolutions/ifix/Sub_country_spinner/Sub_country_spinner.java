package com.smartsoftwaresolutions.ifix.Sub_country_spinner;

public class Sub_country_spinner {

     String CS_ID,CS_Name,CS_name_ar,country_id;

    public Sub_country_spinner(String CS_ID, String CS_Name, String CS_name_ar, String country_id) {
        this.CS_ID = CS_ID;
        this.CS_Name = CS_Name;
        this.CS_name_ar = CS_name_ar;
        this.country_id = country_id;
    }

    public String getCS_ID() {
        return CS_ID;
    }

    public void setCS_ID(String CS_ID) {
        this.CS_ID = CS_ID;
    }

    public String getCS_Name() {
        return CS_Name;
    }

    public void setCS_Name(String CS_Name) {
        this.CS_Name = CS_Name;
    }

    public String getCS_name_ar() {
        return CS_name_ar;
    }

    public void setCS_name_ar(String CS_name_ar) {
        this.CS_name_ar = CS_name_ar;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
