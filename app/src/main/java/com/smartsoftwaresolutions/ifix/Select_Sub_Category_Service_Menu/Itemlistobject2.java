package com.smartsoftwaresolutions.ifix.Select_Sub_Category_Service_Menu;



public class Itemlistobject2 {
    private String ID2;
    private String name2;
    private String desc2;
    private String desc_ar;
    private String image_name;
    private String image_path;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Itemlistobject2(String ID2, String name2, String desc2, String desc_ar, String image_name, String image_path) {
        this.ID2 = ID2;
        this.name2 = name2;
        this.desc2 = desc2;
        this.desc_ar = desc_ar;
        this.image_name = image_name;
        this.image_path=image_path;
    }

    public String getID2() {
        return ID2;
    }

    public void setID2(String ID2) {
        this.ID2 = ID2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc_ar() {
        return desc_ar;
    }

    public void setDesc_ar(String desc_ar) {
        this.desc_ar = desc_ar;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
