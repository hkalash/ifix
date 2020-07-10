package com.smartsoftwaresolutions.ifix.Main_Sub_Category_List;

public class Sub_Category_Button {
    String Sub_Cat_Text,Sub_Cat_Image_path,Sub_Cat_ID;

    public Sub_Category_Button(String sub_Cat_Text, String sub_Cat_Image_path,String sub_Cat_ID) {
        Sub_Cat_Text = sub_Cat_Text;
        Sub_Cat_Image_path = sub_Cat_Image_path;
        Sub_Cat_ID=sub_Cat_ID;
    }

    public String getSub_Cat_ID() {
        return Sub_Cat_ID;
    }

    public void setSub_Cat_ID(String sub_Cat_ID) {
        Sub_Cat_ID = sub_Cat_ID;
    }

    public String getSub_Cat_Text() {
        return Sub_Cat_Text;
    }

    public void setSub_Cat_Text(String sub_Cat_Text) {
        Sub_Cat_Text = sub_Cat_Text;
    }

    public String getSub_Cat_Image_path() {
        return Sub_Cat_Image_path;
    }

    public void setSub_Cat_Image_path(String sub_Cat_Image_path) {
        Sub_Cat_Image_path = sub_Cat_Image_path;
    }
}
