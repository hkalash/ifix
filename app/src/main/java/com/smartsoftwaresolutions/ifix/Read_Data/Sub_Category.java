package com.smartsoftwaresolutions.ifix.Read_Data;

public class Sub_Category {
   // `MS_ID`, `C_ID`, `SC_ID`, `MS_Name`, `MS_Description`, `MS_Name_ar`, `MS_Description_ar`, `Ms_Icon_path`
    String MS_ID;
    String C_ID;
    String SC_ID;
    String MS_Name;
    String MS_Description;
    String MS_Name_ar;
    String MS_Description_ar;
    String Ms_Icon_path;

    public Sub_Category(String MS_ID, String c_ID, String SC_ID,
                        String MS_Name, String MS_Description, String MS_Name_ar,
                        String MS_Description_ar, String ms_Icon_path) {
        this.MS_ID = MS_ID;
        this.C_ID = c_ID;
        this.SC_ID = SC_ID;
        this.MS_Name = MS_Name;
        this.MS_Description = MS_Description;
        this.MS_Name_ar = MS_Name_ar;
        this.MS_Description_ar = MS_Description_ar;
        this.Ms_Icon_path = ms_Icon_path;
    }

    public String getMS_ID() {
        return MS_ID;
    }

    public void setMS_ID(String MS_ID) {
        this.MS_ID = MS_ID;
    }

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }

    public String getSC_ID() {
        return SC_ID;
    }

    public void setSC_ID(String SC_ID) {
        this.SC_ID = SC_ID;
    }

    public String getMS_Name() {
        return MS_Name;
    }

    public void setMS_Name(String MS_Name) {
        this.MS_Name = MS_Name;
    }

    public String getMS_Description() {
        return MS_Description;
    }

    public void setMS_Description(String MS_Description) {
        this.MS_Description = MS_Description;
    }

    public String getMS_Name_ar() {
        return MS_Name_ar;
    }

    public void setMS_Name_ar(String MS_Name_ar) {
        this.MS_Name_ar = MS_Name_ar;
    }

    public String getMS_Description_ar() {
        return MS_Description_ar;
    }

    public void setMS_Description_ar(String MS_Description_ar) {
        this.MS_Description_ar = MS_Description_ar;
    }

    public String getMs_Icon_path() {
        return Ms_Icon_path;
    }

    public void setMs_Icon_path(String ms_Icon_path) {
        Ms_Icon_path = ms_Icon_path;
    }
}
