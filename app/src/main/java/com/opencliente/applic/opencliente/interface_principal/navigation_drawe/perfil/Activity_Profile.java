package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.MainActivity_Auth;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.MainActivity_interface_principal;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_clientes;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.MainActivity_lauch_Store;
import com.opencliente.applic.opencliente.service.ServiseNotify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Profile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //------------------------ Cloud Firestore -----------------------------------------------------
    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore dbProfile = FirebaseFirestore.getInstance();
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    //--------------------- Add Foto ---------------------------------------------------------------
    //-Storage
    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private CircleImageView circleImageViewProfile;

    private Uri urlDescargarFoto;

    //--------------------- Firebase  ----------------------------------------------------------
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    //-------- signOut
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private GoogleApiClient googleApiClient;


    // CARD perfil
    public TextView editTextNombre;
    public  TextView editTextTelefono;
    private LinearLayout layoutTelefono;
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


        ///////////////////////////////////// signOut //////////////////////////////////////////////
        //----Authentication Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ///////////////////// ADD PHOTO ////////////////////////////////////////////////////////////
        mStorage= FirebaseStorage.getInstance().getReference();
        circleImageViewProfile =(CircleImageView) findViewById(R.id.imageView_foto_perfil);

        //---Reference
        editTextNombre =(TextView) findViewById(R.id.textView_name);
        editTextTelefono=(TextView) findViewById(R.id.textView_phone);
        layoutTelefono=(LinearLayout) findViewById(R.id.lineallayout_telefono);
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

        switch (item.getItemId()) {
            case android.R.id.home:
                //-Button retroceso
                finish();
                return true;

            case R.id.action_editar:


                //---Lanzador de activity Auth
                Intent Lanzador1 = new Intent(Activity_Profile.this, Activity_Profile_Edit.class);
                startActivity(Lanzador1);
                finish();

                return true;


            case  R.id.action_eliminar_cuenta:

                new AlertDialog.Builder(Activity_Profile.this)
                        .setIcon(R.mipmap.ic_launcher)
                        //.setTitle(R.string.pregunta_eliminar_negocio_de_lista)
                        .setMessage(R.string.confirma_eliminacion_cuenta)
                        .setPositiveButton(R.string.si, null)
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                // Storage
                                StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                                StorageReference  storageReference= storageReferenceGalery.child(getString(R.string.DB_CLIENTES)).child( firebaseUser.getUid() ).child(getString(R.string.DBS_PERFIL)).child( getResources().getString(R.string.DBS_fotoperfil) );
                                storageReference.delete();

                                //Firestore
                                CollectionReference collectionReference=db.collection(getResources().getString(R.string.DB_CLIENTES)  ).document( firebaseUser.getUid()  ).collection(  getResources().getString(R.string.DB_NEGOCIOS)  );
                                deleteCollection(collectionReference, 100, EXECUTOR);
                                DocumentReference documentReference=db.collection(getResources().getString(R.string.DB_CLIENTES)).document(firebaseUser.getUid());
                                documentReference.delete();



                                ///////////////////////////////// Cerrar Sesion //////////////////////////////////////////////
                                firebaseAuth.signOut();
                                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        if (status.isSuccess()) {

                                            stopService(new Intent(Activity_Profile.this,ServiseNotify.class));

                                            //---Lanzador de activity Auth
                                            Intent intent = new Intent(Activity_Profile.this, MainActivity_Auth.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();

                                            stopService(new Intent(Activity_Profile.this,ServiseNotify.class));

                                            //---Lanzador de activity Auth
                                            Intent intent = new Intent(Activity_Profile.this, MainActivity_Auth.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }}});



                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {}
                        }).show();

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

                        if(aPerfilUsuario.getTelefono().equals("")){
                            editTextTelefono.setText( getResources().getString(R.string.telefono_no_definido) );
                        }else{
                            editTextTelefono.setText(aPerfilUsuario.getTelefono());
                        }


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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    private Task<Void> deleteCollection(final CollectionReference collection, final int batchSize, Executor executor) {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Get the first batch of documents in the collection
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    // Move the query cursor to start after the last doc in the batch
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });

    }

    /**
     * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
     * to avoid blocking/crashing the main thread.
     */
    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (DocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }

}
