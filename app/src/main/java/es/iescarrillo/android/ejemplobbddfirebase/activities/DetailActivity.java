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

    private TextView tvName, tvPowers;
    private Button btnEdit, btnDelete;
    private SuperherosService superherosService;
    private Superhero superhero;

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
        superhero = new Superhero();
        if (intent != null) {
            superhero = (Superhero) intent.getSerializableExtra("superhero");
        }

        tvName.setText(superhero.getName());
        tvPowers.setText(superhero.getPowers().toString());

        btnDelete.setOnClickListener(v -> {
            superherosService.deleteSuperhero(superhero.getId());
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);

        });


        btnEdit.setOnClickListener(v -> {
            Intent intentEdit = new Intent(this, InsertOrEditActivity.class);
            intentEdit.putExtra("edit", true);
            intentEdit.putExtra("superhero", superhero);
            startActivity(intentEdit);
        });
    }
}