package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Profile extends AppCompatActivity {

    //------------------------ Cloud Firestore -----------------------------------------------------
    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore dbProfile = FirebaseFirestore.getInstance();

    //--------------------- Add Foto ---------------------------------------------------------------
    //-Storage
    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private CircleImageView circleImageViewProfile;

    private Uri urlDescargarFoto;

    //--------------------- Firebase AUTH ----------------------------------------------------------
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual



    //--------------------- EditText ---------------------------------------------------------------
    public TextView editTextNombre;
    public  TextView editTextTelefono;
    private ProgressBar progressBar_foto;

    //-String
    private  String data_nombre;
    private  String data_telefono;
    private String UrlfotoPerfil=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        setTitle(R.string.perfil);
        getSupportActionBar().setElevation(0);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////////////////// ADD PHOTO ////////////////////////////////////////////////////////////
        mStorage= FirebaseStorage.getInstance().getReference();
        circleImageViewProfile =(CircleImageView) findViewById(R.id.imageView_foto_perfil);

        //---Reference
        editTextNombre =(TextView) findViewById(R.id.textView_name);
        editTextTelefono=(TextView) findViewById(R.id.textView_phone);
        progressBar_foto=(ProgressBar) findViewById(R.id.progressBar_foto);

        progressBar_foto.setVisibility(View.GONE);




        //carga informacion del perfil
        updateProfile();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {

            //---Lanzador de activity Auth
            Intent Lanzador1 = new Intent(Activity_Profile.this, Activity_Profile_Edit.class);
            startActivity(Lanzador1);
            finish();

            return true;
        }
        //-Button retroceso
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }





    //-Metodo para cargar la foto de perfil
    public void updateProfile(){
        DocumentReference docRef = db.collection(  getString(R.string.DB_CLIENTES) ).document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        //---Carga los datos del usuario
                        String ValueNombre =doc.getString("nombre");
                        String ValueTelefono =doc.getString("telefono");

                        //--Carga la foto de perfil
                        LoadImagePerfil(doc.getString(  getString(R.string.DB_urlfotoPerfil) ));
                        UrlfotoPerfil=doc.getString(  getString(R.string.DB_urlfotoPerfil) );


                        //---Cargamos los datos a los texviewa
                        editTextNombre.setText(ValueNombre);
                        editTextTelefono.setText(ValueTelefono);
                    }
                }
            }
        });
    }
    public void LoadImagePerfil(String uri){
        try{
            Glide.with(Activity_Profile.this)
                    .load(uri)
                    .fitCenter()
                    .centerCrop()
                    .into(circleImageViewProfile);
        }catch (Exception ex){;}
    }




}
