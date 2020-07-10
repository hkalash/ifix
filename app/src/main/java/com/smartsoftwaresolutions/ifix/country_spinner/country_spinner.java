package com.smartsoftwaresolutions.ifix.country_spinner;

public class country_spinner {
    String country_id,country_name,country_name_ar,country_code;

    public country_spinner(String country_id, String country_name, String country_name_ar, String country_code) {
        this.country_id = country_id;
        this.country_name = country_name;
        this.country_name_ar = country_name_ar;
        this.country_code = country_code;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_name_ar() {
        return country_name_ar;
    }

    public void setCountry_name_ar(String country_name_ar) {
        this.country_name_ar = country_name_ar;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
