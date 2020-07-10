package com.smartsoftwaresolutions.ifix.spinner_category;

public class category_spinner {
    String Cat_DI,Cat_Name;

    public category_spinner(String cat_DI, String cat_Name) {
        Cat_DI = cat_DI;
        Cat_Name = cat_Name;
    }

    public String getCat_DI() {
        return Cat_DI;
    }

    public void setCat_DI(String cat_DI) {
        Cat_DI = cat_DI;
    }

    public String getCat_Name() {
        return Cat_Name;
    }

    public void setCat_Name(String cat_Name) {
        Cat_Name = cat_Name;
    }
}
