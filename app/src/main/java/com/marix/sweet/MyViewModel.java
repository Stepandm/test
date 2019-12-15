package com.marix.sweet;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class MyViewModel extends ViewModel{

    MutableLiveData<ArrayList<Dosk>> data = new MutableLiveData<>();

        private ArrayList<Dosk> dosks;
        public void setArrayList(Dosk c){
            if(dosks == null)
                dosks = new ArrayList<>();
            dosks.add(c);
            data.setValue(dosks);

        }

    }

