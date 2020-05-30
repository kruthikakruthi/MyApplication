package com.example.myapplication.Model;

public class DataOfHotel {

    private  String hname,hdescription,himage,pid,time,category,date,haddress,hprice;

    public DataOfHotel() {
    }

    public DataOfHotel(String hname, String hdescription, String himage, String pid, String time, String category, String date, String haddress, String hprice,String Hpeople) {
        this.hname = hname;
        this.hdescription = hdescription;
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

    public String getHdescription() {
        return hdescription;
    }

    public void setHdescription(String hdescription) {
        this.hdescription = hdescription;
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
