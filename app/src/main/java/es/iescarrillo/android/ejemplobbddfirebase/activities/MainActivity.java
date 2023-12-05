package es.iescarrillo.android.ejemplobbddfirebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.adapters.SuperheroAdapter;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;
import es.iescarrillo.android.ejemplobbddfirebase.services.SuperherosService;

public class MainActivity extends AppCompatActivity {

    // Variables globales de la clase
    private SuperheroAdapter adapter;
    private Button btnInsert, btnSearch;
    private EditText etSearch;
    private SuperherosService superherosService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        superherosService = new SuperherosService(getApplicationContext());
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        // Obtenemos la referencial al nodo "superheros"
        DatabaseReference dbSuperheros = FirebaseDatabase.getInstance().getReference()
                .child("superheros");

        // Inicializamos la lista de superhéroes que le vamos a pasar a nuestro adaptador
        List<Superhero> superheros = new ArrayList<>();
        ListView lvSuperheros = findViewById(R.id.lvSuperheros);


       superherosService.getSuperherosOrderByName(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiamos los datos de la lista, sino se duplicarán
                superheros.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
                    Superhero superhero = snapshot.getValue(Superhero.class);
                    superheros.add(superhero);
                }

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                adapter = new SuperheroAdapter(getApplicationContext(), superheros);
                lvSuperheros.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja errores de lectura de la base de datos si es necesario
                Log.w("Firebase", "Error en la lectura de la base de datos", databaseError.toException());
            }
        });

        // Añadimos le listener, que estará en continua ejecución comprobando si hay algún cambio
        /*dbSuperheros.addValueEventListener(new ValueEventListener() {
                        // El método onDataChange se llama cada vez que los datos en la base de datos cambian
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           // Limpiamos los datos de la lista, sino se duplicarán
                           superheros.clear();

                           for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                               // Convierte cada nodo de la base de datos a un objeto Superhero
                               Superhero superhero = snapshot.getValue(Superhero.class);
                               superheros.add(superhero);
                           }

                           // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                           adapter = new SuperheroAdapter(getApplicationContext(), superheros);
                           lvSuperheros.setAdapter(adapter);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           // Maneja errores de lectura de la base de datos si es necesario
                           Log.w("Firebase", "Error en la lectura de la base de datos", databaseError.toException());
                       }
                   });*/

        lvSuperheros.setOnItemClickListener((parent, view, position, id) -> {
            Superhero superhero = (Superhero) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("superhero", superhero);
            startActivity(intent);
        });

        btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(v -> {
            Intent intent = new Intent(this, InsertOrEditActivity.class);
            intent.putExtra("edit", false);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            superherosService.getSuperherosByFilmId(etSearch.getText().toString(), new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    superheros.clear();

                    // Recorremos los nodos hijos
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // Convertimos el nodo al tipo Superhero
                        Superhero superhero = dataSnapshot.getValue(Superhero.class);
                        superheros.add(superhero);
                    }

                    // Pasarle la lista al adaptador
                    adapter = new SuperheroAdapter(getApplicationContext(), superheros);
                    // Le asigno el adaptador al list view
                    lvSuperheros.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("error", error.getDetails());
                }
            });
        });

    }


}