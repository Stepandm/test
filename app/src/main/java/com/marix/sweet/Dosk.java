package com.marix.sweet;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
@IgnoreExtraProperties
public class Dosk implements Serializable {
    public String name;
    public String discription;
    public String weight;
    public String price;
    public String photo;

    Dosk(String n, String d, String w, String pr, String ph){
        name = n;
        discription = d;
        weight = w;
        price = pr;
        photo = ph;
    }

    public static Dosk getDefault(){
        Dosk cake = new Dosk("", "","", "", "");
        return cake;
    }
}
