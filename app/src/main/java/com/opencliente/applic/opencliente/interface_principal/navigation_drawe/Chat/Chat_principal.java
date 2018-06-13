package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador.adapter_recyclerView_Chat_principal;

import java.util.ArrayList;
import java.util.List;

public class Chat_principal extends AppCompatActivity {

    //!!!!!!!!!!!!!!!!!!!!! HOME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public RecyclerView recyclerViewChat;
    public List<adapter_profile_negocio> adapterProfileNegocios;
    public adapter_recyclerView_Chat_principal adapterRecyclerViewChat;
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    //////////////////////////// FIREBASE FIRESTORE CLOUD //////////////////////////////////////////
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    //Referencia para acceso de DB)
    private DatabaseReference databaseReferenceConsultEstado= FirebaseDatabase.getInstance().getReference();
    //Referencia para acceso de DB)
    private DatabaseReference DataBaseRefConsultClient= FirebaseDatabase.getInstance().getReference();

    //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //---img
    private ImageView imageViewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_principal);
        setTitle(R.string.chat);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---Reference
        imageViewChat=(ImageView) findViewById(R.id.imageView_chat);

    }

    @Override
    protected void onStart() {
        super.onStart();

        LoadChat();
    }

    private void LoadChat(){

        //************************************ Home ***********************************************************
        //***********************Click en el item seleccionado********************************************
        recyclerViewChat =(RecyclerView) findViewById(R.id.recyclerView_Chat_principal);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        adapterProfileNegocios =new ArrayList<>();
        adapterRecyclerViewChat =new adapter_recyclerView_Chat_principal(adapterProfileNegocios,this);
        adapterRecyclerViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idBusiness=adapterProfileNegocios.get(recyclerViewChat.getChildAdapterPosition(view)).getId();

                //---Lanzador de activity Auth
                Intent Lanzador1=new Intent(Chat_principal.this,Chat_view.class);
                Lanzador1.putExtra("parametroIdClient",idBusiness);
                startActivity(Lanzador1);
            }});
        recyclerViewChat.setAdapter(adapterRecyclerViewChat);
        //**********************************************************************************************************

        CollectionReference dollecChat=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  );
        dollecChat.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot docChat:task.getResult()){
                        if (docChat.getString(  getString(R.string.DB_ultimo_mensaje)  ) != null) {

                            //--Adaptador del cliente
                            adapter_profile_negocio adapter = docChat.toObject(adapter_profile_negocio.class);

                            //---Si es cliente del negocio carga los datos
                            if (adapter != null ) {

                                //Notifica si ahi o no nuevo mensaje entante
                                Boolean value=docChat.getBoolean(  getString(R.string.DB_mensaje_nuevo)  );
                                if(value==null){value=false;}
                                adapter.setMensaje_nuevo(value);

                                //Si ahi mensajes muestra la conversacion
                                if (!adapter.getUltimo_mensaje().equals("")) {
                                    adapterProfileNegocios.add(adapter);

                                    imageViewChat.setVisibility(View.GONE);

                                }
                            }

                        }

                    }
                    adapterRecyclerViewChat.notifyDataSetChanged();

                }
            }
        });



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
