package com.marix.sweet;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Doska {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String wheel;
    public String podshs;
    public String deks;
}
