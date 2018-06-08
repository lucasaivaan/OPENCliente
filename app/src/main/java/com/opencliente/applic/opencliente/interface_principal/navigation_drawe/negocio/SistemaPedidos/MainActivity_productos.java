package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.MainActivity_lauch_Store;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores.adapter_recyclerView_Sabores;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_producto;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_recyclerView_ProductosNegocio;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_recyclerView_pedidos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_productos extends AppCompatActivity implements AdapterView.OnItemSelectedListener,Serializable {

    //TOllbar
    private EditText editText_Toolbar_Seach;
    private Button button_Delivery;


    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosNegocio adapter_recyclerView_productos;
    private ImageView imageViewFondo;

    // SISTEMA DE PEIDOS
    private Integer iCantidad;
    private Double dPrecio;
    static List<adapter_producto> adapter_productoListPedidos =new ArrayList<>();
    private  Double dTotalPrecio;
    private int iCantidadSabores=0;
    private int iLimiteCantidadSabores=3;
    private Boolean estado_Delivery=false;
    private Boolean estado_RetiroPersonalmente=false;


    // RECYCLERVIEW
    public RecyclerView recyclerViewProductoPedidos;
    public List<adapter_producto> adapter_productoListPedidosRecycler =new ArrayList<>();
    public adapter_recyclerView_pedidos adapter_recyclerView_productosPedidos;


    // Spinner Categoria
    private Spinner spinnerCategoria;// Elementos en Spinner
    private List<String> listCatSpinner = new ArrayList<String>();


    // View Flotante
    private CircleImageView circleImageViewProducto;
    private TextView textView10_nombre;
    public TextView textview_informacion;
    private TextView textview_precio;
    private TextView textview_codigo;
    private LinearLayout layout_contenido_productoVenta;
    private Button button_SeleccionarGustos;


    // Recyclerview categorias
    private RecyclerView recyclerProductos;

    ////////////////////////////  FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private adapter_profile_negocio adapterProfileNegocio;


    //- Storage
    // Create a storage reference from our app
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    // Create a reference to the file to delete
    private StorageReference desertRef;

    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private String urlDescargarFoto;
    private byte[] bytesMapImagen;
    private String idUnicoProducto;

    private String ID_NEGOCIO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_negocios_productos);
        setTitle(" null ");

        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--- Obtenemos los datos pasados por parametro
        ID_NEGOCIO = getIntent().getExtras().getString("ID_NEGOCIO");




        // Reference
        adapter_productoListPedidos=null;
        adapter_productoListPedidos =new ArrayList<>();
        spinnerCategoria=(Spinner) findViewById(R.id.spinner_categorias);
        imageViewFondo=(ImageView) findViewById(R.id.imageView15_fondo2);
        editText_Toolbar_Seach=(EditText) findViewById(R.id.editText2_seach);
        button_Delivery=(Button) findViewById(R.id.button_delivery);
        button_Delivery.setVisibility(View.GONE);

        // Carga los productos
        CargarProductos("");


        // EditText Buscador
        editText_Toolbar_Seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(!editText_Toolbar_Seach.getText().toString().equals("")){

                    // Buscador
                    CargarProductos(editText_Toolbar_Seach.getText().toString());
                }else {

                    if(spinnerCategoria.getSelectedItem() != null){
                        if(!spinnerCategoria.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){
                            CargarProductos(spinnerCategoria.getSelectedItem().toString());
                        }else { CargarProductos("");}
                    } }

            }
        });


        // Firebase  (Configuración de pedidos)
        DocumentReference doc_ConfPedido=db.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection( getResources().getString(R.string.DB_CONFIGURACION) ).document( getResources().getString(R.string.DB_sistema_pedido) );
        doc_ConfPedido.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc_ConfPedido = task.getResult();
                    if (doc_ConfPedido.exists()) {

                        estado_Delivery=  doc_ConfPedido.getBoolean(getResources().getString(R.string.DB_delivery)) ;
                        estado_RetiroPersonalmente= doc_ConfPedido.getBoolean(getResources().getString(R.string.DB_retiro_negocio)) ;

                    }
                }
            }
        });

    }
    //---Algoritmo de busqueda
    private boolean seach(String value,String valueSeach){
        boolean resultado = false;

        if(value == null){
            resultado=  false;
        }else{
            //Convierte los valores String en minuscula para facilitar la busqueda
            value=value.toLowerCase();


            String [] stringsArrayValueBuscador;
            int rdsultado2;

            //------------------   Algoritbo de busqueda --------------------------------------
            stringsArrayValueBuscador=valueSeach.split(" ");
            if(!valueSeach.equals("")){
                for (int i = 0; i < stringsArrayValueBuscador.length; i++){
                    // aqui se puede referir al objeto con arreglo[i];
                    rdsultado2 = value.indexOf(stringsArrayValueBuscador[i].toLowerCase());
                    if(rdsultado2 != -1) {
                        resultado=true;
                    }else{
                        resultado=false;
                        break;
                    }}}
        }

        return resultado;
    }

    //////////////////////////  CARGA PRODUCTOS ///////////////////////////////////////////////////
    public  void CargarProductos(final String categoria){



        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerView_productos);
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosNegocio(adapter_productoList,this);

        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adapter_producto adapterProductoOriginal=adapter_productoList.get(recyclerViewProducto.getChildAdapterPosition(view));


                //////////////////////////////// Cuadro de Dialog //////////////////////////////////
                LayoutInflater inflater = getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.view_producto, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_productos.this);
                builder.setView(dialog);
                final AlertDialog alertDialogHors = builder.show();

                // Reference
                ImageButton imageButton_Exit=(ImageButton) dialog.findViewById(R.id.imageView8);
                circleImageViewProducto=(CircleImageView) dialog.findViewById(R.id.FloatimageView_producto);
                textView10_nombre=(TextView) dialog.findViewById(R.id.textView10_nombre);
                textview_informacion=(TextView) dialog.findViewById(R.id.textview_informacion);
                textview_precio=(TextView) dialog.findViewById(R.id.textview_precio);
                textview_codigo=(TextView) dialog.findViewById(R.id.textview_codigo);
                layout_contenido_productoVenta=(LinearLayout) dialog.findViewById(R.id.layout_contenido_productoVenta);
                button_SeleccionarGustos=(Button) dialog.findViewById(R.id.button_seleccionarGustos);
                button_SeleccionarGustos.setVisibility(View.GONE);
                LinearLayout layout_sistemaPedidos=(LinearLayout) dialog.findViewById(R.id.layout_sistemaPedidos);

                // Cantidad
                Button button_menos=(Button) dialog.findViewById(R.id.button_menos);
                Button button_mas=(Button) dialog.findViewById(R.id.button_mas);
                Button button_agregarProducto =(Button) dialog.findViewById(R.id.button_agregar_producto);
                final TextView textView_cantidad=(TextView) dialog.findViewById(R.id.textView_numeroCamtidad);
                iCantidad=1;
                dPrecio= adapterProductoOriginal.getPrecio() ;

                // control de visibilidad de producto
                //  0= ventas
                //  1= venta de pote de helado con opciones de gustos de helado
                //  3= gustos de helado

                if(adapterProductoOriginal.getTipo() == null){
                    adapterProductoOriginal.setTipo(0);
                }else{
                    if(adapterProductoOriginal.getTipo() == 2){
                        // Oculta codigo , precio y button
                        layout_contenido_productoVenta.setVisibility(View.GONE);
                    }else if (adapterProductoOriginal.getTipo() == 1){
                        button_SeleccionarGustos.setVisibility(View.VISIBLE);
                    }
                }

                // Estado (Sistema PEdidos)
                if(estado_Delivery == false && estado_RetiroPersonalmente == false){
                    layout_sistemaPedidos.setVisibility(View.GONE);
                }



                //Set
                // Carga la imagen de perfil
                if(!adapterProductoOriginal.getUrlimagen().equals("default")){
                    Glide.clear(circleImageViewProducto);
                    Glide.with(MainActivity_productos.this)
                            .load(adapterProductoOriginal.getUrlimagen())
                            .fitCenter()
                            .centerCrop()
                            .into(circleImageViewProducto);
                }else {}

                // Precio
                textview_precio.setText( Double.toString(adapterProductoOriginal.getPrecio()));

                idUnicoProducto=adapterProductoOriginal.getId();
                urlDescargarFoto=adapterProductoOriginal.getUrlimagen();

                textview_codigo.setText(adapterProductoOriginal.getCodigo());

                textView10_nombre.setText(adapterProductoOriginal.getInfo1());
                textview_informacion.setText(adapterProductoOriginal.getInfo2());


                // carga los gustos de la heladeria
                final List<adapter_producto> adapter_Sabores =new ArrayList<>();
                final Map<String,Object> listSaboresSeleccionados= new HashMap<String, Object>();
                iCantidadSabores=0;

                if(adapterProductoOriginal.getTipo() == 1){
                    // BASE DE DATOS Gustos
                    CollectionReference collectionReferenceGustos=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
                    collectionReferenceGustos.whereEqualTo("tipo",2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){

                                // vacia la lista para volver a llenarla
                                adapter_Sabores.removeAll(adapter_Sabores);

                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                    if(documentSnapshot.exists()){
                                        // Constructor
                                        adapter_producto adapterProducto=documentSnapshot.toObject(adapter_producto.class);

                                        adapterProducto.setClickalble( false );
                                        adapter_Sabores.add(adapterProducto);



                                    }
                                }

                            }

                        }
                    });
                }

                // Button OnClick
                imageButton_Exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogHors.dismiss();
                    }
                });
                button_menos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(iCantidad > 1){

                            // Cantidad
                            iCantidad-=1;
                        }

                        // Actuliza el precio
                        textview_precio.setText(  Double.toString( dPrecio*iCantidad )  );

                        textView_cantidad.setText(String.valueOf(iCantidad));

                    }
                });
                button_mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Cantidad
                        iCantidad+=1;

                        // Actuliza y cantidad
                        textview_precio.setText(  Double.toString( dPrecio*iCantidad )  );
                        textView_cantidad.setText(String.valueOf(iCantidad));
                    }
                });
                button_agregarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(adapterProductoOriginal.getTipo() == 1 && listSaboresSeleccionados.size() != 0){

                            final Map<String,Object> informacion_pedido= new HashMap<String, Object>();
                            informacion_pedido.put("cantidad",iCantidad);

                            // Sabores de pote de helado
                            if(adapterProductoOriginal.getTipo() == 1){
                                informacion_pedido.put("gustos",listSaboresSeleccionados);
                            }

                            // Contructor lista pedidos
                            adapterProductoOriginal.setInfopedido(informacion_pedido);

                            // Carga el producto a la liste de pedidos
                            SistemaPedidos(adapterProductoOriginal);
                            alertDialogHors.dismiss();

                        }else if(adapterProductoOriginal.getTipo() == 1 && listSaboresSeleccionados.size() == 0){
                           Toast.makeText(MainActivity_productos.this,getResources().getString(R.string.seleccionar_gustos),Toast.LENGTH_SHORT).show();
                        }

                        if(adapterProductoOriginal.getTipo() == 0){
                            final Map<String,Object> informacion_pedido= new HashMap<String, Object>();
                            informacion_pedido.put("cantidad",iCantidad);

                            // Sabores de pote de helado
                            if(adapterProductoOriginal.getTipo() == 1){
                                informacion_pedido.put("gustos",listSaboresSeleccionados);
                            }

                            // Contructor lista pedidos
                            adapterProductoOriginal.setInfopedido(informacion_pedido);

                            // Carga el producto a la liste de pedidos
                            SistemaPedidos(adapterProductoOriginal);
                            alertDialogHors.dismiss();
                        }else{}



                    }
                });

                button_SeleccionarGustos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
                        LayoutInflater inflater = getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.view_pedido_seleccionar_gustos, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_productos.this);
                        builder.setView(dialoglayout);
                        final AlertDialog alertDialogHors;
                        alertDialogHors = builder.show();

                        // Reference
                        final TextView textViewCantidadSabores=(TextView) dialoglayout.findViewById(R.id.textView43);
                        Button button_OK=(Button) dialoglayout.findViewById(R.id.button14_ok);

                        ////////////////////////////  PRODUCTO
                        final RecyclerView recyclerViewSabores;
                        final adapter_recyclerView_Sabores adapter_recyclerView_Sabores;

                        //---Click en el item seleccionado
                        recyclerViewSabores =(RecyclerView) dialoglayout.findViewById(R.id.recyclerview_gustos);
                        recyclerViewSabores.setLayoutManager(new GridLayoutManager(MainActivity_productos.this,4));
                        //--Adaptadores
                        adapter_recyclerView_Sabores =new adapter_recyclerView_Sabores(adapter_Sabores,MainActivity_productos.this);
                        recyclerViewSabores.setAdapter(adapter_recyclerView_Sabores);

                        adapter_recyclerView_Sabores.notifyDataSetChanged();

                        // OnClick
                        adapter_recyclerView_Sabores.setOnClickListener(new View.OnClickListener() {

                            @Override
                            protected void finalize() throws Throwable {
                                super.finalize();
                            }

                            @Override
                            public void onClick(View view) {



                                //Extrae la id de la reseña
                                final adapter_producto adapterProductoOriginal = adapter_Sabores.get(recyclerViewSabores.getChildAdapterPosition(view));

                                // Selecciona el item
                                if(adapterProductoOriginal.getClickalble() == true ){


                                    adapter_Sabores.get(recyclerViewSabores.getChildAdapterPosition(view)).setClickalble(false);

                                }else if(adapterProductoOriginal.getClickalble() == false){

                                    // condicional ( )
                                    if(iCantidadSabores < iLimiteCantidadSabores){
                                        adapter_Sabores.get(recyclerViewSabores.getChildAdapterPosition(view)).setClickalble(true);

                                    }else{ Toast.makeText(MainActivity_productos.this,"No puedes seleccionar mas de 3 gustos",Toast.LENGTH_SHORT).show(); }

                                }

                                adapter_recyclerView_Sabores.notifyDataSetChanged();


                                iCantidadSabores=0;
                                for(adapter_producto adapterProducto : adapter_Sabores){
                                    if(adapterProducto.getClickalble() == true){
                                        iCantidadSabores+=1;
                                    }
                                }
                                textViewCantidadSabores.setText(String.valueOf( iCantidadSabores )+"-"+String.valueOf( iLimiteCantidadSabores ));


                            }
                            });

                        button_OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                listSaboresSeleccionados.clear();
                                // condicional ( )
                                if(iCantidadSabores == iLimiteCantidadSabores){
                                    for(adapter_producto adapterProducto : adapter_Sabores){
                                        if(adapterProducto.getClickalble() == true){
                                            listSaboresSeleccionados.put(adapterProducto.getId(),adapterProducto.getInfo2());
                                        }
                                    }

                                    // Finaliza
                                    alertDialogHors.dismiss();

                                }else { Toast.makeText(MainActivity_productos.this,"Debes seleccionar 3 gustos ",Toast.LENGTH_SHORT).show(); }

                            }
                        });





                    }
                });


            }});

        recyclerViewProducto.setAdapter(adapter_recyclerView_productos);

        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adapter_productoList.removeAll(adapter_productoList);
                listCatSpinner.removeAll(listCatSpinner);

                //Visibility
                imageViewFondo.setVisibility(View.VISIBLE);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        adapter_producto adapterProducto=doc.toObject(adapter_producto.class);



                        Boolean estadoRepeticion=false;

                        // Recoore el arrayList
                        if(listCatSpinner.size()==0){listCatSpinner.add(getString(R.string.elije_una_categoaria));}
                        for(int x=0;x<listCatSpinner.size();x++) {
                            if(listCatSpinner.get(x).equals(adapterProducto.getSubcategoria())){ estadoRepeticion=true; }
                        }

                        // agrega la categoria al arrayList
                        if(estadoRepeticion == false){
                            if( adapterProducto.getSubcategoria_2() == null){

                                listCatSpinner.add(adapterProducto.getSubcategoria());

                            }else {

                                if(adapterProducto.getSubcategoria_2().equals("") ){
                                    listCatSpinner.add(adapterProducto.getSubcategoria());
                                }else { listCatSpinner.add(adapterProducto.getSubcategoria_2()); }
                            }

                            //Visibility
                            imageViewFondo.setVisibility(View.GONE);
                        }


                        adapterProducto.setId(doc.getId());

                        // Agrega el producto
                        if(categoria.equals("")){
                            adapter_productoList.add(adapterProducto);
                        }else if(!categoria.equals("")){
                            if(seach(adapterProducto.getSubcategoria(),categoria)
                                    || seach(adapterProducto.getSubcategoria(),categoria)
                                    || seach(adapterProducto.getSubcategoria_2(),categoria)
                                    || seach(adapterProducto.getInfo1(),categoria)
                                    || seach(adapterProducto.getInfo2(),categoria)){
                                adapter_productoList.add(adapterProducto);

                                //Visibility
                                imageViewFondo.setVisibility(View.GONE);
                            }else { }
                        }

                    }
                }

                if(adapter_productoList.size()==0){ imageViewFondo.setVisibility(View.VISIBLE);}
                if(listCatSpinner.size()==0){spinnerCategoria.setVisibility(View.GONE);}
                spinnerCategoria.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,listCatSpinner));
                spinnerCategoria.setOnItemSelectedListener(MainActivity_productos.this);

                adapter_recyclerView_productos.notifyDataSetChanged();

            }
        });
    }
    public void SistemaPedidos(adapter_producto adapterProducto){

        adapter_productoListPedidos.add(adapterProducto);

        if(adapter_productoListPedidos.size() > 0){
            button_Delivery.setVisibility(View.VISIBLE);
            button_Delivery.setText( String.valueOf(adapter_productoListPedidos.size())+" "+ getResources().getString(R.string.productos));
        }

    }
    //---------------------------   BUTTON
    public void Button_pedidos(View view){


        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialog=new Dialog(MainActivity_productos.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.view_list_pedidos);
        dialog.show();

        // References
        Button button_RealizarPedido=(Button) dialog.findViewById(R.id.button_realizar_pedido);
        Button button_Cerrar=(Button) dialog.findViewById(R.id.button9_cerrar);

        // Recyclerview
        recyclerViewProductoPedidos=(RecyclerView) dialog.findViewById(R.id.recyclerView_pedidos);
        final TextView textView_PrecioTotal=(TextView) dialog.findViewById(R.id.textView10_perciototal);
        dTotalPrecio=00.00;

        recyclerViewProductoPedidos.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_recyclerView_productosPedidos =new adapter_recyclerView_pedidos(adapter_productoListPedidos,this);

        //OnClick
        adapter_recyclerView_productosPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // Eventos de Recyclerviews
        adapter_recyclerView_productosPedidos.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // Precio toltal
                dTotalPrecio=0.0;

                if(adapter_productoListPedidos.size() != 0){
                    // Recorre el ArrayList de los productos seleccionados
                    for(adapter_producto adapterProducto: adapter_productoListPedidos){

                        // Multiplica el precio del prodcuto  por la cantidad
                        int iCantidad=(int) adapterProducto.getInfopedido().get("cantidad");
                        dTotalPrecio+=adapterProducto.getPrecio()* iCantidad ;

                        // set
                        textView_PrecioTotal.setText(Double.toString(dTotalPrecio));
                        button_Delivery.setText( String.valueOf(adapter_productoListPedidos.size())+" "+ getResources().getString(R.string.productos));
                    }
                }else{

                    // Finaliza el cuadro de dialogo y esconde el button de delivery
                    dialog.dismiss();
                    button_Delivery.setVisibility(View.GONE);
                }

            }
        });
        recyclerViewProductoPedidos.setAdapter(adapter_recyclerView_productosPedidos);
        // Actualiza el RecyclerView
        adapter_recyclerView_productosPedidos.notifyDataSetChanged();


        // Button OnClick
        button_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_RealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent Lanzador1=new Intent(MainActivity_productos.this,MainActivity_negocios_formulario_pedidos.class);
                //Lanzador1.putExtra("ID_NEGOCIO", ID_NEGOCIO);
                Lanzador1.putExtra("ID_NEGOCIO", ID_NEGOCIO);
                startActivity(Lanzador1);

                dialog.dismiss();


            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(adapter_productoListPedidos.size() == 0){
            button_Delivery.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() { finish(); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //-Button retroceso
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_categorias:
                if(!spinnerCategoria.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){
                    CargarProductos(spinnerCategoria.getSelectedItem().toString());
                }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
