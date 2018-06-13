package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores.adaptador_pedido;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores.adapter_recyclerView_lista_pedidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity_negocios_Lista_Pedidos extends AppCompatActivity {

    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewPedidos;
    public List<adaptador_pedido> adapter_PedidosList;
    public adapter_recyclerView_lista_pedidos adapter_recyclerView_pedidos;

    // FIRESTORE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_negocios__lista__pedidos);
        setTitle(getResources().getString(R.string.pedidos));


        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Cargar DAtos
        CargarPedidos();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CargarPedidos(){

        //---Click en el item seleccionado
        recyclerViewPedidos =(RecyclerView) findViewById(R.id.recyclerview_pedidosLista);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_PedidosList =new ArrayList<>();
        adapter_recyclerView_pedidos =new adapter_recyclerView_lista_pedidos(adapter_PedidosList,MainActivity_negocios_Lista_Pedidos.this);

        adapter_recyclerView_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adaptador_pedido adapterProductoOriginal=adapter_PedidosList.get(recyclerViewPedidos.getChildAdapterPosition(view));

                // create an empty array list with an initial capacity
                CharSequence options[] ;

                switch (adapterProductoOriginal.getEstado()){
                    case 0: // Pedido Pendiente
                        options=new CharSequence[] { getResources().getString(R.string.cancelar_pedido)};

                        // alertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_negocios_Lista_Pedidos.this);
                        builder.setCancelable(false);
                        builder.setTitle(getString(R.string.pedido));
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on options[which]
                                switch (which){
                                    case 0:
                                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity_negocios_Lista_Pedidos.this);
                                        dialogo1.setMessage(R.string.pregunta_cancelar_pedido);
                                        dialogo1.setCancelable(false);
                                        dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {

                                                // FIRESTORE CLIENTE
                                                DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(firebaseAuth.getUid()).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(adapterProductoOriginal.getId());
                                                documentReferenceCliente.delete();

                                                // FIRESTORE NEGOCIO
                                                DocumentReference documentReferenceNegocio=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( adapterProductoOriginal.getId_negocio() ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(adapterProductoOriginal.getId());
                                                documentReferenceNegocio.delete();

                                            }
                                        });
                                        dialogo1.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                                // User cancelled the dialog
                                            }
                                        });
                                        dialogo1.show();

                                        break;


                                }
                            }
                        });
                        builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //the user clicked on Cancel
                            }
                        });
                        builder.show();

                        break;
                    case 1: // Pedido en proceso

                        Toast.makeText(MainActivity_negocios_Lista_Pedidos.this,"Pedido en proceso no puedes editar ni cancelar",Toast.LENGTH_LONG).show();

                        break;

                    case 2: // Pedido Enviado

                        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(MainActivity_negocios_Lista_Pedidos.this);
                        dialogo2.setMessage(R.string.pregunta_pedido_recibido);
                        dialogo2.setCancelable(false);
                        dialogo2.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                // MAP DATOS ESTADO
                                Map <String,Object> mapESTADO =new HashMap<String, Object>();
                                mapESTADO.put("estado",5);  //0=(pendiente) 1= (En proceso) 2 =(Enviado) 3=( Listo) 4= (cancelado) 5 =(recibido)


                                // FIRESTORE NEGOCIO
                                DocumentReference documentReferenceNegocio=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( adapterProductoOriginal.getId_negocio() ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(adapterProductoOriginal.getId());
                                documentReferenceNegocio.set(mapESTADO, SetOptions.merge());

                                // FIRESTORE CLIENTE
                                DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(firebaseAuth.getUid()).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(adapterProductoOriginal.getId());
                                documentReferenceCliente.set(mapESTADO, SetOptions.merge());





                            }
                        });
                        dialogo2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                // User cancelled the dialog
                            }
                        });
                        dialogo2.show();

                        break;
                    case 3: // Pedido listo paras retirar del local

                        // VACIO

                        break;
                    case 4: // Pedido Cancelado
                        options=new CharSequence[] { getResources().getString(R.string.eliminar)};

                        // alertDialog
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity_negocios_Lista_Pedidos.this);
                        builder2.setCancelable(false);
                        builder2.setTitle(getString(R.string.pedido));
                        builder2.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on options[which]
                                switch (which){
                                    case 0:
                                        // FIRESTORE CLIENTE
                                        DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(firebaseAuth.getUid()).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(adapterProductoOriginal.getId());
                                        documentReferenceCliente.delete();


                                        break;

                                }
                            }
                        });
                        builder2.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //the user clicked on Cancel
                            }
                        });
                        builder2.show();

                        break;



                }


            }
        });
        recyclerViewPedidos.setAdapter(adapter_recyclerView_pedidos);



        // Firesote
        CollectionReference collectionReference=firestore.collection(  getString(R.string.DB_CLIENTES)  ).document( firebaseAuth.getUid()).collection(  getString(R.string.DB_PEDIDOS)  );
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot task, @Nullable FirebaseFirestoreException e) {

                adapter_PedidosList.removeAll(adapter_PedidosList);

                for (DocumentSnapshot doc : task) {
                    if (doc.exists()) {
                        // Adaptadores
                        adaptador_pedido adapterProducto=doc.toObject(adaptador_pedido.class);
                        adapter_PedidosList.add(adapterProducto);

                    }

                }
                adapter_recyclerView_pedidos.notifyDataSetChanged();

            }
        });


    }
}
