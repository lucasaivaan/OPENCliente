package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_producto;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_recyclerView_ProductosNegocio;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_productos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //TOllbar
    private EditText editText_Toolbar_Seach;


    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosNegocio adapter_recyclerView_productos;
    private ImageView imageViewFondo;

    // Spinner Categoria
    private Spinner spinnerCategoria;// Elementos en Spinner
    List<String> listCatSpinner = new ArrayList<String>();


    // View Flotante
    private CircleImageView circleImageViewProducto;
    private TextView textView10_nombre;
    public TextView textview_informacion;
    private TextView textview_precio;
    private TextView textview_codigo;


    // Recyclerview categorias
    private RecyclerView recyclerProductos;

    ////////////////////////////  FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private adapter_profile_negocio adapterProfileNegocio;


    //- Storage
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    // Create a reference to the file to delete
    StorageReference desertRef;

    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private String urlDescargarFoto;
    private byte[] bytesMapImagen;
    String idUnicoProducto;
    private ProgressBar progressBar_foto;

    private String ID_NEGOCIO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos);
        setTitle(" null ");

        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--- Obtenemos los datos pasados por parametro
        ID_NEGOCIO = getIntent().getExtras().getString("ID_NEGOCIO");




        // Reference

        spinnerCategoria=(Spinner) findViewById(R.id.spinner_categorias);
        imageViewFondo=(ImageView) findViewById(R.id.imageView15_fondo2);
        editText_Toolbar_Seach=(EditText) findViewById(R.id.editText2_seach);


        // Carga los productos
        Recycler_Producto("");


        editText_Toolbar_Seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(!editText_Toolbar_Seach.getText().toString().equals("")){

                    // Buscador
                    Recycler_Producto(editText_Toolbar_Seach.getText().toString());
                }else {

                    if(spinnerCategoria.getSelectedItem() != null){
                        if(!spinnerCategoria.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){
                            Recycler_Producto(spinnerCategoria.getSelectedItem().toString());
                        }else { Recycler_Producto("");}
                    } }

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
    public  void Recycler_Producto(final String categoria){



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
                final Dialog dialog=new Dialog(MainActivity_productos.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.view_producto);
                dialog.show();

                // Reference
                ImageButton imageButton_Exit=(ImageButton) dialog.findViewById(R.id.imageView8);
                circleImageViewProducto=(CircleImageView) dialog.findViewById(R.id.FloatimageView_producto);
                textView10_nombre=(TextView) dialog.findViewById(R.id.textView10_nombre);
                textview_informacion=(TextView) dialog.findViewById(R.id.textview_informacion);
                textview_precio=(TextView) dialog.findViewById(R.id.textview_precio);
                textview_codigo=(TextView) dialog.findViewById(R.id.textview_codigo);
                progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar2);
                progressBar_foto.setVisibility(View.GONE);

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

                idUnicoProducto=adapterProductoOriginal.getId();
                urlDescargarFoto=adapterProductoOriginal.getUrlimagen();

                textview_codigo.setText(adapterProductoOriginal.getCodigo());

                textView10_nombre.setText(adapterProductoOriginal.getInfo1());
                textview_informacion.setText(adapterProductoOriginal.getInfo2());
                textview_precio.setText(Double.toString(adapterProductoOriginal.getPrecio()));

                imageButton_Exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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

    //---------------------------   BUTTON


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
                    Recycler_Producto(spinnerCategoria.getSelectedItem().toString());
                }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
