package com.example.myapplication.Holder;

public class RetrieveDataHolder {


    private  String dname,description,image,pid,time,category,date,address,price;


    public RetrieveDataHolder() {
    }
    public RetrieveDataHolder(String dname, String description, String image, String pid, String time, String category, String date, String address,String price) {
        this.dname = dname;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.time = time;
        this.category = category;
        this.date = date;
        this.address = address;
        this.price=price;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
