package es.iescarrillo.android.ejemplobbddfirebase.services;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.iescarrillo.android.ejemplobbddfirebase.models.Film;

public class FilmsService {

    DatabaseReference database;

    public FilmsService(Context context){
        database = FirebaseDatabase.getInstance().getReference().child("films");
    }


    public void insertFilm(Film film){
        DatabaseReference newReference = database.push();
        film.setId(newReference.getKey());
        film.setDuration(90.0);

        newReference.setValue(film);
    }
}
