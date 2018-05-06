package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.MainActivity_interface_principal;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador.AdapterMensajes;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador.Mensaje;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador.MensajeRecibir;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_view extends AppCompatActivity {

    //---------------------------------- Firestore -------------------------------------------------
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    //Auth
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //---------------------------------- Elementos -------------------------------------------------
    private adapter_profile_negocio aPerfilNegocio;
    private CircleImageView fotoPerfil;
    private TextView textViewNombreNegocio;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private AdapterMensajes adapter;
    private String fotoPerfilCadena;
    private String idNegocio ="null";
    private  String nombreCliente;
    private String idCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        setTitle("");

        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //- Extraxion de dato pasado por parametro
        idNegocio =getIntent().getStringExtra("parametroIdClient");


        //- Referencias
        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfilClienteChat);
        textViewNombreNegocio = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        fotoPerfilCadena = "";

        //---Carga el chat
        Load();





    }

    //- Metodo que carga el chat
    protected void Load() {


        // Informmacion del perfil del negocio
        DocumentReference docPerfil=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(idNegocio);
        docPerfil.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot perfil=task.getResult();
                    if(perfil.exists()){
                        aPerfilNegocio=perfil.toObject(adapter_profile_negocio.class);
                        //--Carga el nombre Negocio
                        textViewNombreNegocio.setText(aPerfilNegocio.getNombre_negocio());

                        int id= icono.getIconLogoCategoria(aPerfilNegocio.getCategoria(),Chat_view.this);
                        fotoPerfil.setImageResource(id);

                        //Imagen del negocio
                        if(aPerfilNegocio.getImagen_perfil().equals("default")){
                            // icono
                            int id_icon= icono.getIconLogoCategoria(aPerfilNegocio.getCategoria(),Chat_view.this);
                            fotoPerfil.setBackgroundResource(id_icon);
                        }else{
                            //-Carga la imagen de perfil
                            Glide.with(Chat_view.this).load(aPerfilNegocio.getImagen_perfil()).into(fotoPerfil);

                        }
                    }
                }
            }
        });

        // Informacion del perfil del cliente
        DocumentReference docProfileCliente=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
        docProfileCliente.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot perfil=task.getResult();
                    if(perfil.exists()){
                        //-Nombre del cliente
                        nombreCliente=perfil.getString(  getString(R.string.DB_nombre)  );
                        //-Uri foto perfil
                        fotoPerfilCadena=perfil.getString(  getString(R.string.DB_urlfotoPerfil)  );
                    }
                }
            }
        });

        // Referencia DB del Negocio
        final DocumentReference AddMensajeChatNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(idNegocio).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());

        // Referencia DB del cliente
        final DocumentReference AddMensajeChatCliente=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(idNegocio);

        //Cambiar el estado de mensaje recibido a false
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(  getString(R.string.DB_mensaje_nuevo)  ,false);
        AddMensajeChatCliente.set(objectMap, SetOptions.merge());


        // Adaptador chat
        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);


        //-Button Enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-Si el cliente no tiene foto se le asigna una foto por ddefecto
                if(fotoPerfilCadena == null || fotoPerfilCadena.equals("")){
                    fotoPerfilCadena="default";
                }

                if(!txtMensaje.getText().toString().equals("")){

                    //Guarda informacion del mensaje
                    Map<String , Object> dataClient=new HashMap<>();
                    dataClient.put( getString(R.string.DB_mensaje) ,txtMensaje.getText().toString());
                    dataClient.put( getString(R.string.DB_timestamp) , FieldValue.serverTimestamp());
                    dataClient.put( getString(R.string.DB_urlfotoPerfil) ,fotoPerfilCadena);
                    dataClient.put( getString(R.string.DB_type_mensaje) ,"2");
                    dataClient.put( getString(R.string.DB_nombre) ,nombreCliente);



                    AddMensajeChatCliente.collection(  getString(R.string.DB_CHAT)  ).document().set(dataClient);
                    AddMensajeChatNegocio.collection(  getString(R.string.DB_CHAT)  ).document().set(dataClient);

                    //Notifica de un mensaje nuevo y el ultimo mensaje enviado
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(  getString(R.string.DB_ultimo_mensaje)  ,txtMensaje.getText().toString());
                    AddMensajeChatCliente.set(objectMap,SetOptions.merge());

                    objectMap.put(  getString(R.string.DB_mensaje_nuevo)  ,true);
                    AddMensajeChatNegocio.set(objectMap,SetOptions.merge());


                    txtMensaje.setText("");
                }

                }});

        //-Escucha Evento RecyclerView
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        // Referencia coleccion de mensajes
        CollectionReference collectionReference=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(idNegocio).collection(  getString(R.string.DB_CHAT)  );

        // Escucha Eventos en la Database
        collectionReference.orderBy(  getString(R.string.DB_timestamp)  , Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            //-Si el cliente no tiene foto se le asigna una foto por ddefecto
                            if(fotoPerfilCadena == null || fotoPerfilCadena.equals("")){
                                fotoPerfilCadena="default";
                            }

                            MensajeRecibir m = dc.getDocument().toObject(MensajeRecibir.class);
                            if(dc.getDocument().getDate(  getString(R.string.DB_timestamp)  ) != null){
                                adapter.addMensaje(m);
                            }
                            break;
                        case MODIFIED:
                            //-Si el cliente no tiene foto se le asigna una foto por ddefecto
                            if(fotoPerfilCadena == null || fotoPerfilCadena.equals("")){
                                fotoPerfilCadena="default";
                            }

                            MensajeRecibir m2 = dc.getDocument().toObject(MensajeRecibir.class);
                            if(dc.getDocument().getDate(  getString(R.string.DB_timestamp)  ) != null){
                                adapter.addMensaje(m2);
                            }
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_mensajes, menu);
        return true;
    }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_vaciar_chat) {

            DocumentReference documentReferenceChat=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(idNegocio);
            deleteCollection(documentReferenceChat.collection(  getString(R.string.DB_CHAT)  ), 50, EXECUTOR);

            //Elimina el dato del ultimo mensaje de referencia
            Map<String,Object> update=new HashMap<>();
            update.put(  getString(R.string.DB_ultimo_mensaje)  ,FieldValue.delete());
            documentReferenceChat.update(update);

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

    //- Posiciona la vista de recyclerView al ultimo mensaje recibido
    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }



}