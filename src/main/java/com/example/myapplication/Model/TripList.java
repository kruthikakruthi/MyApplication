package com.example.myapplication.Model;

public class TripList

{


    private  String pid,dname,discount,address,peoples,price;

    public TripList() {
    }

    public TripList(String pid, String dname, String discount, String address, String peoples,String price) {
        this.pid = pid;
        this.dname = dname;
        this.discount = discount;
        this.address = address;
        this.peoples = peoples;
        this.price=price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
