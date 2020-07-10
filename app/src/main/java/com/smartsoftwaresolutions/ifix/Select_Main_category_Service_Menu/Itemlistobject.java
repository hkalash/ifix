package com.smartsoftwaresolutions.ifix.Select_Main_category_Service_Menu;

public class Itemlistobject {
     private String ID;
    private String name;
    private String desc;
    private String desc_ar;
    private String Image_name;
    private String Image_path;

    public String getImage_path() {
        return Image_path;
    }

    public void setImage_path(String image_path) {
        Image_path = image_path;
    }

    public Itemlistobject(String ID, String name, String desc, String desc_ar, String image_name,String image_path) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.desc_ar = desc_ar;
        this.Image_name = image_name;
        this.Image_path=image_path;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc_ar() {
        return desc_ar;
    }

    public void setDesc_ar(String desc_ar) {
        this.desc_ar = desc_ar;
    }

    public String getImage_name() {
        return Image_name;
    }

    public void setImage_name(String image_name) {
        Image_name = image_name;
    }
}
