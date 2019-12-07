package com.example.call.Model;

import java.util.Date;

public class Contact {
    private int Id;
    private String Name;
    private String Phone_number;
    private String Type;
    private Date CallDate;
    private Integer Duration;

    public Contact() {
    }

    public Contact(int id, String name, String phone_number, String type, Date date, Integer duration) {
        Id = id;
        Name = name;
        Phone_number = phone_number;
        Type = type;
        CallDate = date;
        Duration = duration;
    }

    public Contact(String name, String phone_number, String type, Date date, Integer duration) {
        Name = name;
        Phone_number = phone_number;
        Type = type;
        CallDate = date;
        Duration = duration;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Date getCallDate() {
        return CallDate;
    }

    public void setCallDate(Date date) {
        CallDate = date;
    }

    public Integer getDuration() {
        return Duration;
    }

    public void setDuration(Integer duration) {
        Duration = duration;
    }
}
