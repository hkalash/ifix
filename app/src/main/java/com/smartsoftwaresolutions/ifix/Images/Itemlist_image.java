package com.smartsoftwaresolutions.ifix.Images;

public class Itemlist_image {
    String image_uri;
    int photo;

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public Itemlist_image(String image_name) {
        this.image_uri = image_name;
        this.photo = photo;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
