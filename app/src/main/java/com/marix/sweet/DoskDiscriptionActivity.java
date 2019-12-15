package com.marix.sweet;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DoskDiscriptionActivity extends AppCompatActivity {
    private TextView name;
    private TextView discription;
    private TextView price;
    private TextView weight;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosk_discription);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //инициализируем элементы ui

        name = (TextView) findViewById(R.id.discriptionName);
        discription = (TextView) findViewById(R.id.discriptionText);
        price = (TextView) findViewById(R.id.discriptionPrice);
        weight = (TextView) findViewById(R.id.discriptionWeight);
        photo = (ImageView) findViewById(R.id.discriptionPhoto);

        // заполняем переданными данными

        name.setText(getIntent().getStringExtra("name"));
        price.setText("Цена: " + getIntent().getStringExtra("price"));
        weight.setText("Вес: " + getIntent().getStringExtra("weight"));
        discription.setText(getIntent().getStringExtra("discription"));
        Picasso.get().load(getIntent().getStringExtra("photo")).into(photo);

        //ставим слушатель

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoskDiscriptionActivity.this, CreateDoskActivity.class);
                startActivity(i);
            }
        });
    }

}
