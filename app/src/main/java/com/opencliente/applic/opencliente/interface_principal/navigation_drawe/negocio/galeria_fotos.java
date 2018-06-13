package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adaptador_foto;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_recyclerView_Fotos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class galeria_fotos extends AppCompatActivity {

    // Notificacion sin fotos
    private TextView textViewNoti_SinFotos;


    ////////////////////////////  Galeria
    public RecyclerView recyclerViewProducto;
    public List<adaptador_foto> adaptadorFotoList;
    public adapter_recyclerView_Fotos adapter_recyclerView_Galeria;
    private ImageView imageViewFondo;


    private String ID_NEGOCIO;

    // Firebase
    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fotos);
        setTitle(getResources().getString(R.string.fotos).toUpperCase());

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //--- Obtenemos los datos pasados por parametro
        ID_NEGOCIO = getIntent().getExtras().getString("ID_NEGOCIO");

        //Reference
        textViewNoti_SinFotos=(TextView) findViewById(R.id.textViewNoti_SinFotos);

        // Cargar las fotos
        if(ID_NEGOCIO != null){
            cargar_fotos(ID_NEGOCIO);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //-Button retroceso
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ViewFoto( String sUrlFoto, String sComentario){

        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_galery_photo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(galeria_fotos.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogHors;
        alertDialogHors=builder.show();

        // Reference
        // Reference
        ImageView imageView_Cerrar=(ImageButton) dialoglayout.findViewById(R.id.imageButton_close);
        ImageView imageView_Foto=(ImageView) dialoglayout.findViewById(R.id.imageView_galery_foto);
        final TextView textView_Comentario =(TextView) dialoglayout.findViewById(R.id.textView_comentario);
        final ProgressBar progressBar=(ProgressBar) dialoglayout.findViewById(R.id.progressBar2);

        // Condiciones
        if(sComentario.equals("")){
            textView_Comentario.setVisibility(View.GONE);
        }


        imageView_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cierra la vista
                alertDialogHors.dismiss();
            }
        });
        // Imagen
        Glide.with(galeria_fotos.this)
                .load(sUrlFoto)
                .fitCenter()
                .centerCrop()
                .into(imageView_Foto);

        // Comentario
        textView_Comentario.setText(sComentario);




    }
    public void cargar_fotos(String ID_NEGOCIO){

        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerview_galeria);
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adaptadorFotoList =new ArrayList<>();
        adapter_recyclerView_Galeria =new adapter_recyclerView_Fotos(adaptadorFotoList);
        adapter_recyclerView_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Adaptador
                adaptador_foto adapterNegocioPerfil= new adaptador_foto();
                adapterNegocioPerfil=adaptadorFotoList.get(recyclerViewProducto.getChildAdapterPosition(view));

                // funcion de viste de la imagen
                ViewFoto(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario());
            }
        });

        recyclerViewProducto.setAdapter(adapter_recyclerView_Galeria);

        //  BASE DE DATOS
        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_GALERIA_FOTOS));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adaptadorFotoList.removeAll(adaptadorFotoList);
                textViewNoti_SinFotos.setVisibility(View.VISIBLE);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        adaptador_foto adapterProducto=doc.toObject(adaptador_foto.class);

                        if(!adapterProducto.getId().equals("favorito")){
                            adaptadorFotoList.add(adapterProducto);

                            textViewNoti_SinFotos.setVisibility(View.GONE);
                        }


                    }

                }
                adapter_recyclerView_Galeria.notifyDataSetChanged();

            }
        });

    }
}
