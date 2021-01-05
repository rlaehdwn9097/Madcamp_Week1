package com.example.myapplication;

public class ListItem {

    private String name;
    private String phone;
    private String Email;
    private String image;

    public ListItem(){
    }

    public ListItem(String name, String phone, String Email, String image){
        this.name = name;
        this.phone = phone;
        this.Email = Email;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
