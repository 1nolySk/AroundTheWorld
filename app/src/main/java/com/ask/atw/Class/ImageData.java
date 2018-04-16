package com.ask.atw.Class;

import java.util.Calendar;

/**
 * Created by DELL on 29-Mar-18.
 */

public class ImageData {

    String place;
    String url;
    String desc;
    String author;
    String name;
    String gender;

    public ImageData() {
    }

    public ImageData(String place, String url, String desc, String author, String name,String gender) {
        this.place = place;
        this.url = url;
        this.desc = desc;
        this.author = author;
        this.name = name;
        this.gender = gender;

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }
    public String getGender() {
        return gender;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(name));

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DATE);

        String fina= Integer.toString(mDay).concat("-"+Integer.toString(mMonth)+"-"+Integer.toString(mYear));

        return fina;
    }
}
