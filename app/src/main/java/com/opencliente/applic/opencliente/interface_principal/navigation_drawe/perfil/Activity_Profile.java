package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_clientes;

import java.util.HashMap;
import java.util.Map;

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



    // CARD perfil
    public TextView editTextNombre;
    public  TextView editTextTelefono;
    private ProgressBar progressBar_foto;
    private String sCardLayout;
    private int iPuntos=500;


    // Button
    private Button button_Perzonalizar;
    private Button button_Guardar;

    // Layout view
    private LinearLayout layout_styles;
    private LinearLayout layout_Cardstylo;

    // CardView Style (4)
    private CardView style_1;
    private CardView style_2;
    private CardView style_3;
    private CardView style_4;
    private CardView style_5;



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

        button_Guardar=(Button) findViewById(R.id.button_Guardar);
        button_Guardar.setVisibility(View.GONE);
        button_Perzonalizar=(Button) findViewById(R.id.button_Perzonalizar);

        layout_styles=(LinearLayout) findViewById(R.id.layout_styles);
        layout_styles.setVisibility(View.GONE);
        layout_Cardstylo=(LinearLayout) findViewById(R.id.layout_Cardstylo);

        style_1=(CardView) findViewById(R.id.style_1);
        style_2=(CardView) findViewById(R.id.style_2);
        style_3=(CardView) findViewById(R.id.style_3);
        style_4=(CardView) findViewById(R.id.style_4);
        style_5=(CardView) findViewById(R.id.style_5);

        // stylos de las tarjetas
        final String sStyle_1="card_style_1";
        final String sStyle_2="card_style_2";
        final String sStyle_3="card_style_3";
        final String sStyle_4="card_style_4";
        final String sStyle_5="card_style_5";

        // SetOnclick
        style_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control de visibilidad
                button_Guardar.setVisibility(View.VISIBLE);
                button_Perzonalizar.setVisibility(View.GONE);

                //Asignacion del stilo
                layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_1);
                sCardLayout=sStyle_1;

            }
        });
        style_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control de visibilidad
                button_Guardar.setVisibility(View.VISIBLE);
                button_Perzonalizar.setVisibility(View.GONE);

                //Asignacion del stilo
                layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_2);
                sCardLayout=sStyle_2;

            }
        });
        style_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if( iPuntos >= 100 ){
                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_3);
                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);

                    sCardLayout=sStyle_3;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_3);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                 }



            }
        });
        style_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( iPuntos >= 100 ){

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_4);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);


                    sCardLayout=sStyle_4;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_4);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                }



            }
        });
        style_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( iPuntos >= 100 ){

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_5);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);


                    sCardLayout=sStyle_5;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_5);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                }



            }
        });



        //carga informacion del perfil
        Carga_PerfilUsuario();

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



    public void Button_guardar(View view){

        // Control de visibilidad
        button_Perzonalizar.setVisibility(View.VISIBLE);
        button_Guardar.setVisibility(View.GONE);
        layout_styles.setVisibility(View.GONE);

        //Cloud Firestore
        // Guardad los datos del negocio
        final Map<String, Object> cardBusiness = new HashMap<>();
        cardBusiness.put("cardlayout",sCardLayout);

        DocumentReference documentReference=db.collection(  getString(R.string.DB_CLIENTES)  ).document( firebaseUser.getUid() );
        documentReference.set(cardBusiness, SetOptions.merge());

        //Actualizando informacion en la lista de clientes de  los negocios
        CollectionReference collecNegocios=db.collection(  getString(R.string.DB_CLIENTES)  ).document( firebaseUser.getUid() ).collection(  getString(R.string.DB_NEGOCIOS)  );
        collecNegocios.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){

                        if(doc.getString("id") != null){
                            DocumentReference docNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(doc.getId()).collection(  getString(R.string.DB_CLIENTES)  ).document(  firebaseUser.getUid()  );
                            docNegocio.set(cardBusiness, SetOptions.merge());
                        }

                    }
                }

            }
        });

    }
    public void button_Perzonalizar(View view){

        // Control de visibilidad
        layout_styles.setVisibility(View.VISIBLE);

        // Color stylos
        //style_1.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        //style_2.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        //style_3.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        //style_4.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        //style_5.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));

    }


    //-Metodo para cargar la foto de perfil
    public void Carga_PerfilUsuario(){
        DocumentReference docRef = db.collection(  getString(R.string.DB_CLIENTES) ).document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){

                        adapter_profile_clientes aPerfilUsuario=doc.toObject(adapter_profile_clientes.class);


                        if(!aPerfilUsuario.getUrlfotoPerfil().equals("default")){
                            //--Carga la foto de perfil
                            Glide.with(Activity_Profile.this)
                                    .load(aPerfilUsuario.getUrlfotoPerfil())
                                    .fitCenter()
                                    .centerCrop()
                                    .into(circleImageViewProfile);
                        }

                        //---Cargamos los datos a los texviewa
                        editTextNombre.setText(aPerfilUsuario.getNombre());
                        editTextTelefono.setText(aPerfilUsuario.getTelefono());

                        //stilo de tarjeta
                        Context context = layout_Cardstylo.getContext();
                        if(aPerfilUsuario.getCardlayout() !=null && aPerfilUsuario.getCardlayout() != ""){

                            if(aPerfilUsuario.getCardlayout().equals("default")){
                                //layout_Cardstylo.setBackgroundColor(Color.parseColor(aPerfilUsuario.getColor()));
                            }else {
                                int id = context.getResources().getIdentifier( aPerfilUsuario.getCardlayout(), "mipmap", context.getPackageName());
                                layout_Cardstylo.setBackgroundResource(id);

                                // nombre color
                                //editTextNombre.setTextColor(Color.parseColor(aPerfilUsuario.getColor()));
                            }

                        }else{
                            //layout_Cardstylo.setBackgroundColor(Color.parseColor(aPerfilUsuario.getColor()));
                        }
                    }
                }
            }
        });
    }





}
