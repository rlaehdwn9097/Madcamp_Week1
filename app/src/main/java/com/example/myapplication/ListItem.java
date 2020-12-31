package com.example.myapplication;

public class ListItem {

    private String name;
    private String phone;
    private String Email;

    public ListItem(){
    }

    public ListItem(String name, String phone, String Email){
        this.name = name;
        this.phone = phone;
        this.Email = Email;
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
