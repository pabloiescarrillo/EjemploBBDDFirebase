package es.iescarrillo.android.ejemplobbddfirebase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;
import es.iescarrillo.android.ejemplobbddfirebase.services.SuperherosService;

public class DetailActivity extends AppCompatActivity {

    TextView tvName, tvPowers;
    Button btnEdit, btnDelete;
    SuperherosService superherosService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        superherosService = new SuperherosService(getApplicationContext());

        tvName = findViewById(R.id.tvName);
        tvPowers = findViewById(R.id.tvPowers);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        Intent intent = getIntent();
        Superhero superhero = new Superhero();
        if (intent != null) {
            superhero = (Superhero) intent.getSerializableExtra("superhero");
        }

        tvName.setText(superhero.getName());
        tvPowers.setText(superhero.getPowers().toString());

        final Superhero finalSuperhero = superhero;
        btnDelete.setOnClickListener(v -> {
            superherosService.deleteSuperhero(finalSuperhero.getId());
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);

        });

        btnEdit.setOnClickListener(v -> {
            Intent intentEdit = new Intent(this, InsertOrEditActivity.class);
            intentEdit.putExtra("edit", true);
            intentEdit.putExtra("superhero", finalSuperhero);
            startActivity(intentEdit);
        });
    }
}