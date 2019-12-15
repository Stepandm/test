package com.marix.sweet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateDoskActivity extends AppCompatActivity {
        private FirebaseFirestore firestore;

        private Database database = App.getInstance().getDatabase();
        private Dao dao = database.dao();
        private AppExecuter executer = new AppExecuter();

        private ArrayAdapter<String> dekaAdapter;
        private ArrayAdapter<String> wheelsAdapte;
        private ArrayAdapter<String> podshAdapter;

        private Spinner dekaType;
        private Spinner wheels;
        private Spinner podsh;
        private EditText deka;
        private Button send;

        private final String TAG = "Mes";

        private ArrayList<String> podshs = new ArrayList<>();
        private ArrayList<String> wheel = new ArrayList<>();
        private ArrayList<String> deks = new ArrayList<>();

    public CreateDoskActivity() throws ExecutionException, InterruptedException {
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_dosk);
            //инициализация бд
            firestore = FirebaseFirestore.getInstance();

            //инициализация элементов ui
            dekaType = (Spinner) findViewById(R.id.dekaType);
            podsh = (Spinner) findViewById(R.id.podsh);
            wheels = (Spinner) findViewById(R.id.wheels);
            deka = (EditText) findViewById(R.id.deka);
            send = (Button) findViewById(R.id.sendButton);
            //инициализация адаптеров
            dekaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            wheelsAdapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            podshAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            //присвоение адаптеров
            dekaType.setAdapter(dekaAdapter);
            podsh.setAdapter(podshAdapter);
            wheels.setAdapter(wheelsAdapte);
            //ставим слушатель на кнопку
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //проверка на заполнение
                    if(!deka.getText().toString().equals("")) {
                        //отправляем данные на сервер
                        Map<String, Object> doska = new HashMap<>();
                        doska.put("size", Integer.parseInt(deka.getText().toString()));
                        doska.put("deka", dekaType.getSelectedItem().toString());
                        doska.put("podsh", podsh.getSelectedItem().toString());
                        doska.put("wheels", wheels.getSelectedItem().toString());


                        firestore.collection("order").add(doska).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CreateDoskActivity.this, "Заказ отправлен.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }else Toast.makeText(CreateDoskActivity.this, "Введите длинну доски", Toast.LENGTH_SHORT).show();

                }
            });
            //получаем с сервера или бд элементы выпадающего списка
            getItem();
        }

        private void getItem(){
        //проверка на заполненость room
            int lenght = dao.getAll().size();
            if(lenght == 0)
            firestore.collection("query")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //заполняем room и рекурсивно вызываем функцию
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Map<String, Object> data = document.getData();
                                    deks = (ArrayList<String>) data.get("deka");
                                    wheel = (ArrayList<String>) data.get("wheels");
                                    podshs = (ArrayList<String>) data.get("podsh");
                                    Doska dosks = new Doska();
                                    for(int i = 0; i < maxSize(deks.size(), wheel.size(), podshs.size()); i++){
                                        if (i < deks.size()){
                                            dosks.deks = deks.get(i);
                                        }
                                        else dosks.deks = "";
                                        if (i < wheel.size()){
                                            dosks.wheel = wheel.get(i);
                                        }
                                        else dosks.wheel = "";
                                        if (i < podshs.size()){
                                            dosks.podshs = podshs.get(i);
                                        }
                                        else dosks.podshs = "";



                                        dao.insert(dosks);
                                    }
                                        getItem();


                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
            else {
                //вытаскиваем из room элементы выподающего списка
                List<Doska> dosks = dao.getAll();
                for (int i = 0; i < dosks.size(); i++){
                    if (!dosks.get(i).deks.equals("")){
                        dekaAdapter.add(dosks.get(i).deks);
                        Log.d(TAG, "getItem: "+dosks.get(i).deks);
                    }

                    if (!dosks.get(i).podshs.equals("")){
                        podshAdapter.add(dosks.get(i).podshs);
                        Log.d(TAG, "getItem: "+dosks.get(i).podshs);
                    }

                    if (!dosks.get(i).wheel.equals("")){
                        wheelsAdapte.add(dosks.get(i).wheel);
                        Log.d(TAG, "getItem: "+dosks.get(i).wheel);
                    }

                }
                //уведомлем адаптеры об изменение
                dekaAdapter.notifyDataSetChanged();
                wheelsAdapte.notifyDataSetChanged();
                podshAdapter.notifyDataSetChanged();
            }


        }


        private class AppExecuter extends AsyncTask<Void, Void, List<Doska>>{

            @Override
            protected List<Doska> doInBackground(Void... voids) {
                List<Doska> doskss = dao.getAll();
                return doskss;
            }
        }

        private int maxSize(int size1, int size2, int size3){
            if (size1 > size2 && size1 > size3){
                return size1;
            }else if (size2 > size3){
                return size2;
            }else return size3;
        }

}
