package es.iescarrillo.android.ejemplobbddfirebase.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import es.iescarrillo.android.ejemplobbddfirebase.R;
import es.iescarrillo.android.ejemplobbddfirebase.models.Superhero;
import es.iescarrillo.android.ejemplobbddfirebase.services.SuperherosService;

public class InsertOrEditActivity extends AppCompatActivity {

    // Declaramos los componentes de la vista
    private EditText etName, etPowers;
    private Switch swActive;
    private ImageView ivAvatar;
    private Button btnSave, btnCancel;

    // Declaramos los servicios que vamos a necesitar
    private SuperherosService superherosService;

    private Boolean editMode;
    private Superhero superhero;

    // Creamos la referencia a Firebase Storage
    private StorageReference storageReference;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_edit);

        // Inicializamos los servicios
        superherosService = new SuperherosService(getApplicationContext());

        // Inicializamos la referencia a nuestra aplicación Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference().child("avatars/");

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
            if(!superhero.getAvatar().isEmpty()) // Cargamos la imagen con la librería de Picasso si tiene imagen asociada
                Picasso.get().load(Uri.parse(superhero.getAvatar())).into(ivAvatar);
        }

        // Funcionales de los botones
        ivAvatar.setOnClickListener(v -> {
            openGallery();
        });

        btnSave.setOnClickListener(v -> {
            String idSuperhero;

            // Asignamos los valores introducidos
            superhero.setName(etName.getText().toString());
            superhero.setPowers(Arrays.asList(etPowers.getText().toString().split(",")));
            superhero.setActive(swActive.isChecked());

            String url="";
            // Llamamos al servicio
            if(editMode) {
                superherosService.updateSuperhero(superhero);
                uploadImage(superhero.getId());
            } else{
                idSuperhero = superherosService.insertSuperhero(superhero);
                uploadImage(idSuperhero);
            }

            Log.i("url", url);

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        });

        btnCancel.setOnClickListener(v -> {
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        });

    }

    // Médoto para cargar al imagen en Firebase Storage
    private void uploadImage(String idSuperhero){
        /* Llamamos al método getImagenUri creado por nosotros para obtener la URI de una imagen
        almacenada en un ImagenView, le pasamos también el id del superhérore ya que el nombre
        de la imagen almacenada se identificará con el id del superhérore
        * */
        Uri file = getImageUri(this, ivAvatar, idSuperhero);

        // Obtenemos la nueva referencia
        StorageReference storageRefSuperhero = storageReference.child(idSuperhero);

        // Llamamos al método putFile, el cuál recibe un objeto URI, el que hemos obtenido anteriormente
        storageRefSuperhero.putFile(file).addOnFailureListener(new OnFailureListener() {
            // Método que se ejecutará si se produce un fallo
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            // Método que se ejecutará si todo sale bien
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                if (uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url= uri.toString(); // Aquí vamos a obtener la url de la imagen para actualizar la propiedad del superheroe

                            superhero.setAvatar(url);
                            superherosService.updateSuperhero(superhero);
                        }
                    });
                }
                Toast.makeText(getApplicationContext(), "Save superhero", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Método para abrir la galería
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        imageActivityResultLauncher.launch(intent);
    }

    // Método para cargar la imagen seleccionada en la galería en el ImagenView
    private ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri selectedImage = Objects.requireNonNull(data).getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.decodeStream(imageStream);
                    ivAvatar.setImageURI(selectedImage);// Cargamos la imagen en el imagenView
                }
            });

    // Método para obtener la URI de la imagen de un ImageView
    private Uri getImageUri(Context context, ImageView imageView, String name) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, null);
        return Uri.parse(path);
    }

    // Método donde cargaremos todos los componentes de nuestra vista
    private void getComponentFromView(){
        etName = findViewById(R.id.etName);
        etPowers = findViewById(R.id.etPowers);
        swActive = findViewById(R.id.swActive);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }
}