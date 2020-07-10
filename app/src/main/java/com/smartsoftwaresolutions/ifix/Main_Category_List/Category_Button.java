package com.smartsoftwaresolutions.ifix.Main_Category_List;

public class Category_Button {
    String Category_ID,Category_Image_path;

    private String desc;




    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public Category_Button(String category_ID, String category_Image_path, String desc) {
        Category_ID = category_ID;
        Category_Image_path = category_Image_path;

        this.desc = desc;

    }
//    public Category_Button(String category_ID, String category_Image_path) {
//        Category_ID = category_ID;
//        Category_Image_path = category_Image_path;
//    }

    public String getCategory_ID() {
        return Category_ID;
    }

    public void setCategory_ID(String category_ID) {
        Category_ID = category_ID;
    }

    public String getCategory_Image_path() {
        return Category_Image_path;
    }

    public void setCategory_Image_path(String category_Image_path) {
        Category_Image_path = category_Image_path;
    }
}
