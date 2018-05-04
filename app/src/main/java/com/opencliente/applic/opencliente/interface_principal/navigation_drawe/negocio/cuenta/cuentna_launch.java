package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.adaptadores.adapter_productos;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.adaptadores.adapter_recyclerView_producos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class cuentna_launch extends AppCompatActivity {

    ////////////////////////////////////////// Firestore ///////////////////////////////////////////
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    //Auth
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //String
    private String idBusiness;
    DecimalFormat formato = new DecimalFormat("#.00");


    //////////////////////////////// RecyclerView productos ////////////////////////////////////////
    //variables
    private double priceTotal=0.0;
    private double pricePartial=0.0;
    private TextView textViewPriceTotal;
    private LinearLayout linearLayoutPagoParcial;
    private TextView textViewPagoParcial;
    private TextView notific_cuenta_vacia;

    public RecyclerView recyclerViewProduts;
    public List<adapter_productos> adapter_productsList;
    public adapter_recyclerView_producos adapter_recyclerView_products;
    //Firebase
    private DatabaseReference databaseReferenceRecyclrViewProducts= FirebaseDatabase.getInstance().getReference();
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_launch);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idBusiness=getIntent().getStringExtra("parametroIdStrore");

        //Referene
        textViewPriceTotal=(TextView) findViewById(R.id.textView_total);
        linearLayoutPagoParcial=(LinearLayout) findViewById(R.id.linealloyout_pago_parcial);
        textViewPagoParcial=(TextView) findViewById(R.id.textView_value_pago_parcial);
        notific_cuenta_vacia=(TextView) findViewById(R.id.textView19);
        notific_cuenta_vacia.setVisibility(View.GONE);


        LoaddataRecyclerViewCuenta();
    }

    public void LoaddataRecyclerViewCuenta(){

        ////////////////////////////////// RecyclerView Product ////////////////////////////////////
        recyclerViewProduts =(RecyclerView) findViewById(R.id.recyclerview_cuenta_productos);
        recyclerViewProduts.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_productsList =new ArrayList<>();
        adapter_recyclerView_products =new adapter_recyclerView_producos(adapter_productsList);
        //---Click en el item seleccionado
        adapter_recyclerView_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) { }});
        recyclerViewProduts.setAdapter(adapter_recyclerView_products);




        // Firestore
        CollectionReference collecCuenta=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(idBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CUENTA)  );
        collecCuenta.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot producto : task.getResult()) {

                        //Carga los datos en el adaptador
                        adapter_productos adapterProductos=producto.toObject(adapter_productos.class);

                        if(adapterProductos.getPrecio() != null){
                            //Agrega el adaptador con los datos en la lista de recyclerView
                            adapter_productsList.add(adapterProductos);
                            //-Suma el precio
                            priceTotal+=adapterProductos.getPrecio();
                        }
                    }
                    //-Resta el pago parcial del pago total

                    if(priceTotal>=1){

                        // Pago parcial
                        DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(idBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc=task.getResult();
                                    if(doc.exists()){
                                        if(doc.getDouble("pagoparcial") != null){
                                            if(doc.getDouble("pagoparcial")>=1){
                                                pricePartial=doc.getDouble("pagoparcial");

                                                // Habilita la notificacion de pago parcial
                                                linearLayoutPagoParcial.setVisibility(View.VISIBLE);
                                                textViewPagoParcial.setText("$"+String.valueOf(pricePartial));

                                                // Resta el pago parcial del pago total
                                                priceTotal-=pricePartial;

                                                textViewPriceTotal.setText("$"+formato.format(priceTotal));
                                                adapter_recyclerView_products.notifyDataSetChanged();
                                            }
                                        }else{
                                            textViewPriceTotal.setText("$"+formato.format(priceTotal));
                                            adapter_recyclerView_products.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        });

                    }else{
                        notific_cuenta_vacia.setVisibility(View.VISIBLE);
                        textViewPriceTotal.setText("$0");
                        linearLayoutPagoParcial.setVisibility(View.GONE);
                        //--------------------- Firebase -------------------------------------------
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //datos usuaio actual
                        firebaseDatabase.getReference().child(  getString(R.string.DB_NEGOCIOS)  ).child(idBusiness).child(  getString(R.string.DB_CLIENTES)  ).child(firebaseUser.getUid()).child(  getString(R.string.DB_CUENTA)  ).child("pagoparcial").removeValue();
                    }
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
