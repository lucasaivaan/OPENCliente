package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opencliente.applic.opencliente.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Profile_Edit extends AppCompatActivity {

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
    public EditText editTextNombre;
    public  EditText editTextTelefono;
    private ProgressBar progressBar_foto;

    //-String
    private  String data_nombre;
    private  String data_telefono;
    private String UrlfotoPerfil=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile__edit);
        setTitle(R.string.perfil);
        getSupportActionBar().setElevation(0);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////////////////// ADD PHOTO ////////////////////////////////////////////////////////////
        mStorage= FirebaseStorage.getInstance().getReference();
        circleImageViewProfile =(CircleImageView) findViewById(R.id.imageView_foto_perfil);

        //---Reference
        editTextNombre =(EditText) findViewById(R.id.editText_nombre);
        editTextTelefono=(EditText) findViewById(R.id.editText_telefono);
        progressBar_foto=(ProgressBar) findViewById(R.id.progressBar_foto);

        progressBar_foto.setVisibility(View.GONE);




        //carga informacion del perfil
        updateProfile();

    }

    public  void ButtonGuardar(View view){



        //--------------- Comprobacion que los datos no esten vacios -------------------------------
        if(!editTextNombre.getText().toString().equals("")){

            String data_name=editTextNombre.getText().toString();
            // pone la iniciales en mayuscula
            char[] caracteres = data_name.toCharArray();
            caracteres[0] = Character.toUpperCase(caracteres[0]);
            // el -2 es para evitar una excepción al caernos del arreglo
            for (int i = 0; i < data_name.length()- 2; i++){
                // Es 'palabra'
                if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                    // Reemplazamos
                    caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
            }
            data_name=new String(caracteres);

            //-------------- extraccion de datos -------------------------------------------------------
            final Map<String, Object> valuePROFILE = new HashMap<>();
            valuePROFILE.put("nombre",data_name);
            valuePROFILE.put("telefono",editTextTelefono.getText().toString());

            // Si la el valor uri es distinto a  null actualiza  la foto de perfil
            if(urlDescargarFoto != null ){valuePROFILE.put(  getString(R.string.DB_urlfotoPerfil)  , urlDescargarFoto.toString());}
            else {
                //  si el campo url de la foto esta vacio o no existe le asigna un valor por defecto
                if(UrlfotoPerfil==null || UrlfotoPerfil.equals("") ){ valuePROFILE.put(  getString(R.string.DB_urlfotoPerfil) , "default");}

            }


            // Guardando dato en la base de datos
            db.collection(  getString(R.string.DB_CLIENTES) ).document(firebaseUser.getUid()).set(valuePROFILE, SetOptions.merge());

            //Actualizando informacion en la lista de clientes de  los negocios
            CollectionReference collecNegocios=db.collection(  getString(R.string.DB_CLIENTES) ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS) );
            collecNegocios.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (DocumentSnapshot doc:task.getResult()){
                            DocumentReference docNegocio=db.collection(  getString(R.string.DB_NEGOCIOS) ).document(doc.getId()).collection(  getString(R.string.DB_CLIENTES) ).document(firebaseUser.getUid());
                            docNegocio.set(valuePROFILE, SetOptions.merge());
                        }
                    }

                }
            });

            finish();
        }else{
            //--- message en el caso que el campo nombre este vacio
            Toast.makeText(getApplicationContext(),R.string.campo_nombre_vacio, Toast.LENGTH_SHORT).show();
        }


        //-------------------------- finaliza el actyvity ------------------------------------------
    }

    public  void ButtonADDFotho(View view){


        //-metodo para seleccion una imagen en la galeria
        Intent intent=new Intent(Intent.ACTION_PICK);

        //-Tipo de elementos a seleccionar
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(intent,GALLLERY_INTENT);

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
            Glide.with(Activity_Profile_Edit.this)
                    .load(uri)
                    .fitCenter()
                    .centerCrop()
                    .into(circleImageViewProfile);
        }catch (Exception ex){;}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ////////////////// Comprobacion que se alla seleccionado una foto //////////////////////////
        if(requestCode==GALLLERY_INTENT && resultCode==RESULT_OK){

            progressBar_foto.setVisibility(View.VISIBLE); //progressBar cargando foto de perfil
            circleImageViewProfile.setVisibility(View.GONE);
            Uri uri=data.getData();

            //Reduce el tamaño de la imagen
            Bitmap bitmap=null;
            try {bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            }catch (IOException e){e.printStackTrace();}

            BitmapFactory.Options bmOptions= new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds=true;
            bmOptions.inSampleSize=1;
            bmOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
            bmOptions.inJustDecodeBounds=false;

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,30,baos);
            final byte[] foto=baos.toByteArray();

            //-Crea la carpeta dentro del Storage
            StorageReference filePath=mStorage.child(  getString(R.string.DB_CLIENTES) ).child(firebaseUser.getUid()).child(  getString(R.string.DBS_PERFIL) ).child("fotoperfil");

            UploadTask uploadTask= filePath.putBytes(foto);

            //Proceso de subida del archivo
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(Activity_Profile_Edit.this,R.string.se_actualizado_su_foto_de_perfil,Toast.LENGTH_SHORT).show();
                    progressBar_foto.setVisibility(View.GONE);
                    circleImageViewProfile.setVisibility(View.VISIBLE);

                    //-Obtiene la url de la foto
                    urlDescargarFoto=taskSnapshot.getDownloadUrl();
                    LoadImagePerfil(urlDescargarFoto.toString());

                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
