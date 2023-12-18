package es.iescarrillo.android.ejemplobbddfirebase.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;

public class SuperherosService {

    // Referencia de la base de datos
    private DatabaseReference database;

    // Constructor vacío
    public SuperherosService(Context context){
        // Inicializamos la basde de datos y su referencia al nodo de superhéroes
        database = FirebaseDatabase.getInstance().getReference().child("superheros");
    }

    public String insertSuperhero(Superhero superhero) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        superhero.setId(newReference.getKey()); // Asigna el ID generado automáticamente
        superhero.setAvatar("avatars/"+superhero.getId());

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(superhero);
        return superhero.getId();
    }

    public void updateSuperhero(Superhero superhero) {
        database.child(superhero.getId()).setValue(superhero);
    }

    public void deleteSuperhero(String id) {
        database.child(id).removeValue();
    }

    public void deleteSuperhero(Superhero superhero){
        database.child(superhero.getId()).removeValue();
    }
    
    public void getSuperherosOrderByName(ValueEventListener listener){
        Query query = database.orderByChild("name");
        query.addValueEventListener(listener);
    }

    public void getSuperherosGetSpiderman(ValueEventListener listener){
        Query query = database.orderByChild("name").equalTo("Spider-Man2");
        query.addValueEventListener(listener);
    }

    public void getSuperherosByName(String name, ValueEventListener listener){
        Query query = database.orderByChild("name").equalTo(name);
        query.addValueEventListener(listener);
    }

    public void getSuperherosByFilmId(String filmId, ValueEventListener listener){
        Query query = database.orderByChild("filmId").equalTo(filmId);
        query.addValueEventListener(listener);
    }

}
