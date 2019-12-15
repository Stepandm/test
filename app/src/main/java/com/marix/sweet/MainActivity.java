package com.marix.sweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView doskList;
    private MyViewModel viewModel;

    private DataAdapter doskAdapter = new DataAdapter(MainActivity.this);
    private FirebaseFirestore db;
    final String TAG = "Mes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //подключение бд
        db = FirebaseFirestore.getInstance();
        //создаю ViewModel
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        doskList = (RecyclerView) findViewById(R.id.doskList);
        //Запрос бд при условии, если приложение запущенно в первый раз
        if (savedInstanceState == null) {
            db.collection("sweets")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String, Object> data = document.getData();
                                    if(data.get("name") != null) {
                                        Dosk c = new Dosk(data.get("name").toString(), data.get("discription").toString(), data.get("weight").toString(), data.get("price").toString(), data.get("photo").toString());
                                        viewModel.setArrayList(c);
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
        //проверка ориентации уструйства
        if(!isLandscape()) {
            doskList.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else {
            doskList.setLayoutManager(new GridLayoutManager(this, 5));
        }

        //загрузка данных из viewmodel
        viewModel.data.observe(this, new Observer<ArrayList<Dosk>>() {
            @Override
            public void onChanged(ArrayList<Dosk> cakes) {
                doskAdapter.add(cakes);
            }
        });

        doskList.setAdapter(doskAdapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateDoskActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isLandscape(){
        int rotate = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotate){
            case Surface
                    .ROTATION_0:
                return false;
            default:
                return true;
        }
    }

}
