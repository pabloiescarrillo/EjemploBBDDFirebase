package es.iescarrillo.android.ejemplobbddfirebase.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.ejemplobbddfirebase.adapters.SuperheroAdapter;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;

public class SuperherosService {

    // Referencia de la base de datos
    DatabaseReference database;

    // Constructor vacío
    public SuperherosService(Context context){
        // Inicializamos la basde de datos y su referencia al nodo de superhéroes
        database = FirebaseDatabase.getInstance().getReference().child("superheros");
    }

    public void insert(Superhero superhero) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        superhero.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(superhero);
    }

    public void updateSuperhero(Superhero superhero) {
        database.child(superhero.getId()).setValue(superhero);
    }

    public void deleteSuperhero(String id) {
        database.child(id).removeValue();
    }
}
