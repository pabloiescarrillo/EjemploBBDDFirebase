package es.iescarrillo.android.ejemplobbddfirebase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Arrays;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;
import es.iescarrillo.android.ejemplobbddfirebase.services.SuperherosService;

public class InsertOrEditActivity extends AppCompatActivity {

    // Declaramos los componentes de la vista
    private EditText etName, etPowers;
    private Switch swActive;
    private Button btnSave, btnCancel;

    // Declaramos los servicios que vamos a necesitar
    private SuperherosService superherosService;

    private Boolean editMode;
    private Superhero superhero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_edit);

        // Inicializamos los servicios
        superherosService = new SuperherosService(getApplicationContext());

        // Obtenemos los componentes de la vista
        getComponentFromView();

        superhero = new Superhero();
        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("edit",false);
        // Si estamos editando cargamos los datos
        if(editMode){
            // Obtenemos el objeto Superhero que viene en el Intent
            superhero = (Superhero) intent.getSerializableExtra("superhero");
            etName.setText(superhero.getName());
            etPowers.setText(superhero.getPowers().toString().replace("[", "").replace("]", ""));
            swActive.setChecked(superhero.isActive());
        }

        // Funcionales de los botones
        btnSave.setOnClickListener(v -> {
            // Asignamos los valores introducidos
            superhero.setName(etName.getText().toString());
            superhero.setPowers(Arrays.asList(etPowers.getText().toString().split(",")));
            superhero.setActive(swActive.isChecked());

            // Llamamos al servicio
            if(editMode)
                superherosService.updateSuperhero(superhero);
            else
                superherosService.insertSuperhero(superhero);

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        });

        btnCancel.setOnClickListener(v -> {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        });
    }

    // Método donde cargaremos todos los componentes de nuestra vista
    private void getComponentFromView(){
        etName = findViewById(R.id.etName);
        etPowers = findViewById(R.id.etPowers);
        swActive = findViewById(R.id.swActive);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }
}