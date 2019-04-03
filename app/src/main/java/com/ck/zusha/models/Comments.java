package com.ck.zusha.models;

public class Comments {

    public String comment,date,name,time;
    public Comments(){

    }
    public Comments(String comment,String date,String name,String time){
                this.comment=comment;
                this.date=date;
                this.name=name;
                this.time=time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
