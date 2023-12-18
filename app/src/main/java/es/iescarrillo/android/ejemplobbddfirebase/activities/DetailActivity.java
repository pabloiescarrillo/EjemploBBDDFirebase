package es.iescarrillo.android.ejemplobbddfirebase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;
import es.iescarrillo.android.ejemplobbddfirebase.services.SuperherosService;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvPowers;
    private Button btnEdit, btnDelete;
    private ImageView ivAvatarDetail;
    private SuperherosService superherosService;
    private Superhero superhero;

    // Creamos la referencia a Firebase Storage
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        superherosService = new SuperherosService(getApplicationContext());

        // Inicializamos la referencia a nuestra aplicación Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference().child("avatars/");


        tvName = findViewById(R.id.tvName);
        tvPowers = findViewById(R.id.tvPowers);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        ivAvatarDetail = findViewById(R.id.ivAvatarDetail);

        Intent intent = getIntent();
        superhero = new Superhero();
        if (intent != null) {
            superhero = (Superhero) intent.getSerializableExtra("superhero");
        }

        tvName.setText(superhero.getName());
        tvPowers.setText(superhero.getPowers().toString());
        if(!superhero.getAvatar().isEmpty())
            Picasso.get().load(superhero.getAvatar()).into(ivAvatarDetail);

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