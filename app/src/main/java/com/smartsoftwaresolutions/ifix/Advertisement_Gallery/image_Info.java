package com.smartsoftwaresolutions.ifix.Advertisement_Gallery;

public class image_Info {
    String Ad_Name;
    String  AD_Image;

    public image_Info(String ad_Name, String AD_Image) {
        Ad_Name = ad_Name;
        this.AD_Image = AD_Image;
    }

    public String getAd_Name() {
        return Ad_Name;
    }

    public void setAd_Name(String ad_Name) {
        Ad_Name = ad_Name;
    }

    public String getAD_Image() {
        return AD_Image;
    }

    public void setAD_Image(String AD_Image) {
        this.AD_Image = AD_Image;
    }
}
