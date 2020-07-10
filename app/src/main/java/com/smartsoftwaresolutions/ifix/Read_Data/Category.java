package com.smartsoftwaresolutions.ifix.Read_Data;

public class Category {
    String SC_ID;
    String SC_Descption;
    String SC_Description_ar;
    String SC_Icon_path;

    public Category(String SC_ID, String SC_Descption, String SC_Description_ar, String SC_Icon_path) {
        this.SC_ID = SC_ID;
        this.SC_Descption = SC_Descption;
        this.SC_Description_ar = SC_Description_ar;
        this.SC_Icon_path = SC_Icon_path;
    }

    public String getSC_ID() {
        return SC_ID;
    }

    public void setSC_ID(String SC_ID) {
        this.SC_ID = SC_ID;
    }

    public String getSC_Descption() {
        return SC_Descption;
    }

    public void setSC_Descption(String SC_Descption) {
        this.SC_Descption = SC_Descption;
    }

    public String getSC_Description_ar() {
        return SC_Description_ar;
    }

    public void setSC_Description_ar(String SC_Description_ar) {
        this.SC_Description_ar = SC_Description_ar;
    }

    public String getSC_Icon_path() {
        return SC_Icon_path;
    }

    public void setSC_Icon_path(String SC_Icon_path) {
        this.SC_Icon_path = SC_Icon_path;
    }
}
