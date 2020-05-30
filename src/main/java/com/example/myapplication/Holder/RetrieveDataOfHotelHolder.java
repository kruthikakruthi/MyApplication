package com.example.myapplication.Holder;

public class RetrieveDataOfHotelHolder {

    private  String hname,hdescription,himage,pid,time,category,date,haddress,hprice;

    public RetrieveDataOfHotelHolder() {
    }

    public RetrieveDataOfHotelHolder(String hname, String description, String himage, String pid, String time, String category, String date, String haddress, String hprice) {
        this.hname = hname;
        this.hdescription = description;
        this.himage = himage;
        this.pid = pid;
        this.time = time;
        this.category = category;
        this.date = date;
        this.haddress = haddress;
        this.hprice = hprice;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getdescription() {
        return hdescription;
    }

    public void setdescription(String description) {
        this.hdescription = description;
    }

    public String getHimage() {
        return himage;
    }

    public void setHimage(String himage) {
        this.himage = himage;
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

    public String getHaddress() {
        return haddress;
    }

    public void setHaddress(String haddress) {
        this.haddress = haddress;
    }

    public String getHprice() {
        return hprice;
    }

    public void setHprice(String hprice) {
        this.hprice = hprice;
    }
}
