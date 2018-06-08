package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_horario;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_ofertas_negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_clientes;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_Ofertas;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_Reseñas;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_Servicios;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_horario;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_review;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_servicios_negocio;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adaptador_foto;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.Chat_view;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.MainActivity_productos;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.cuentna_launch;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil.Activity_Profile_Edit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_lauch_Store extends AppCompatActivity implements OnMapReadyCallback {

    //Animacion Loadiing
    private LottieAnimationView lottieAnimationView_load;

    ///////////////////////////////////// PROFILE ///////////////////////////////////////////////
    private  adapter_profile_negocio adapterProfileNegocio;
    private adapter_profile_clientes adapterProfileCliente;

    ////////////////////////////////////// OFERTAS ///////////////////////////////////////////////
    public RecyclerView recyclerViewOfertas;
    public List<adapter_ofertas_negocio> adapterProfileNegociosOfertas;
    public adapter_recyclerView_Ofertas adapterRecyclerViewOfertas;

    private CardView cardViewOfert;
    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////// SERVICIOS ///////////////////////////////////////////////
    public RecyclerView recyclerViewServicios;
    public List<adapter_servicios_negocio> adapterProfileNegociosServicios;
    public adapter_recyclerView_Servicios adapterRecyclerViewServicios;

    private CardView cardViewService;
    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////// Horarios ///////////////////////////////////////////////
    //Referencia para acceso de DB)
    private DatabaseReference DataBaseRefHorarios= FirebaseDatabase.getInstance().getReference();

    public RecyclerView recyclerViewHorarios;
    public List<adapter_horario> adapterHorarios;
    public adapter_recyclerView_horario adapterRecyclerViewHorarios;

    //vista flotante
    private AlertDialog alertDialogHors;

    //TexgtView
    private TextView notific_no_hors;
    /////////////////////////////////////// Galeria ////////////////////////////////////////////////
    //---Galeria de fotos
    private ImageView imageViewFoto1;
    private ImageView imageViewFoto2;
    private ImageView imageViewFoto3;
    private ImageView imageViewFoto4;
    private ImageView imageViewFoto5;
    private CardView cardViewFoto1;
    private CardView cardViewFoto2;
    private CardView cardViewFoto3;
    private CardView cardViewFoto4;
    private CardView cardViewFoto5;


    private DatabaseReference firebaseDatabaseGalery;

    private LinearLayout Lineallayout_galery;
    private TextView textView_galery_noFoto;
    private CardView cardview_Galeria;

    ////////////////////////////////////// RESEÑAS ////////////////////////////////////////////////

    private TextView tNot_Reseña;
    private CardView cardViewReseñas;
    private LinearLayout linearLayout_reseña_calificacion;
    private  LinearLayout linearLayout_Edittext_reseña;

    // RatingBar
    private RatingBar ratingBar;
    private TextView tRatingbar;

    //valores
    private int estrellas=0;

    ///////////////////////////////////// Google Maps   ////////////////////////////////////////////
    private GoogleMap mMap;

    private SupportMapFragment mapFragment;
    //Bitmap iconMaker
    private BitmapDescriptor iconKiosco;
    private BitmapDescriptor iconAlmacen;
    private BitmapDescriptor iconPizzeria;

    ////////////////////////////////////// Content /////////////////////////////////////////////////

    //Tolbar
    private CircleImageView imageView_iconStrore;
    private TextView textView_name_store;
    private AppBarLayout appBarLayoutL;


    //Animation
    private LottieAnimationView animation_new_message;


    //---Button
    public LinearLayout buttonAddNegocio;
    public LinearLayout lTolbar;
    public Button buttonChat;
    private Button button_VerProductos;


    //--TextView
    //public TextView textViewTitulo;
    public TextView textViewPais;
    public TextView textViewProvincia;
    public TextView textViewCiudad;
    public TextView textViewDireccion;
    public TextView textViewCodigoPostal;
    public TextView textViewTelefono;
    public TextView textViewWeb;

    //--Descripcion del negocio
    private CardView cardview_descripcion;
    private TextView textViewDescripcion;

    //---String id
    public String IdBusiness;
    public String IDClient;
    public String numeroTelefonoBusiness;
    //---Dooble
    public double Latitud, Longitud;

    //Animation
    private Animation animationUptodown;

    //---------------------------- Cloud Firestore -------------------------------------------------
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore dbNegocio = FirebaseFirestore.getInstance();
    FirebaseFirestore dbCliente = FirebaseFirestore.getInstance();

    //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface_negocio);



        //-------------------------------- Toolbar -------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);

        //Content
        imageView_iconStrore=(CircleImageView) findViewById(R.id.circleImagen_perfil_tolbar);
        textView_name_store=(TextView) findViewById(R.id.name_store);
        appBarLayoutL=(AppBarLayout) findViewById(R.id.appBarLayout);

        //Anim Loading
        lottieAnimationView_load=(LottieAnimationView) findViewById(R.id.animation_view_load);


        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //---Reference
        // Button
        button_VerProductos=(Button) findViewById(R.id.button7);

        //animacion
        animationUptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);//aparece de arriba a su posision

        //Toolbar
        buttonAddNegocio = (LinearLayout) findViewById(R.id.button_agregar_negocio);
        buttonAddNegocio.setVisibility(View.GONE);
        lTolbar = (LinearLayout) findViewById(R.id.LinealLayout_tolbar);
        lTolbar.setVisibility(View.GONE);
        buttonChat= (Button) findViewById(R.id.button_chat);

        //Profile
        textViewPais = (TextView) findViewById(R.id.textView_pais);
        textViewProvincia = (TextView) findViewById(R.id.textView_provincia);
        textViewCiudad = (TextView) findViewById(R.id.textView_ciudad);
        textViewDireccion = (TextView) findViewById(R.id.textView_direccion);
        textViewCodigoPostal = (TextView) findViewById(R.id.textView_codigo_postal);
        textViewTelefono = (TextView) findViewById(R.id.textView_telefono);
        textViewWeb = (TextView) findViewById(R.id.textView_web);

        textViewDescripcion=(TextView) findViewById(R.id.textView_descripcion);
        cardview_descripcion=(CardView) findViewById(R.id.cardview_descripcion);

        // Galery
        imageViewFoto1=(ImageView) findViewById(R.id.button_galary_1);
        imageViewFoto2=(ImageView) findViewById(R.id.button_galary_2);
        imageViewFoto3=(ImageView) findViewById(R.id.button_galary_3);
        imageViewFoto4=(ImageView) findViewById(R.id.button_galary_4);
        imageViewFoto5=(ImageView) findViewById(R.id.button_galary_5);
        cardViewFoto1=(CardView) findViewById(R.id.card_foto1);
        cardViewFoto2=(CardView) findViewById(R.id.card_foto2);
        cardViewFoto3=(CardView) findViewById(R.id.card_foto3);
        cardViewFoto4=(CardView) findViewById(R.id.card_foto4);
        cardViewFoto5=(CardView) findViewById(R.id.card_foto5);

        Lineallayout_galery=(LinearLayout) findViewById(R.id.Lineallayout_galery);
        textView_galery_noFoto=(TextView) findViewById(R.id.textView_galery_noFoto);
        cardview_Galeria=(CardView) findViewById(R.id.cardview_Galeria);


        //CardView Service and Ofert
        cardViewOfert=(CardView) findViewById(R.id.Card_Oferta);
        cardViewOfert.setVisibility(View.GONE);
        cardViewService=(CardView) findViewById(R.id.Card_Service);
        cardViewService.setVisibility(View.GONE);

        // Reseñas
        tNot_Reseña=(TextView) findViewById(R.id.reseña_not_sin_reseña);
        cardViewReseñas=(CardView) findViewById(R.id.cardview_muro);

        // RatingBar
        ratingBar=(RatingBar) findViewById(R.id.ratingBar2);
        tRatingbar=(TextView) findViewById(R.id.textView29_reseña);




        linearLayout_reseña_calificacion=(LinearLayout) findViewById(R.id.linearLayout_reseña_calificacion);
        linearLayout_reseña_calificacion.setVisibility(View.GONE);

        linearLayout_Edittext_reseña =(LinearLayout) findViewById(R.id.lineallayout_publicar);
        linearLayout_Edittext_reseña.setVisibility(View.GONE);

        //Anmation Lottin
        animation_new_message=(LottieAnimationView) findViewById(R.id.anim_round);


        //--- Obtenemos los datos pasados por parametro
        IdBusiness = getIntent().getExtras().getString("parametroId");
        Latitud = 0;
        Longitud = 0;
        Latitud = getIntent().getExtras().getDouble("parametroLatitud");
        Longitud = getIntent().getExtras().getDouble("parametroLongitud");

        //----------------------------------Maps----------------------------------------------------
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity_lauch_Store.this);

        //---Caraga los datos
        LoadData();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_eliminar_negocio) {
            // Eliminacion del negocio
            Delete_negocio();
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
        getMenuInflater().inflate(R.menu.menu_lauch_negocio, menu);
        return true;
    }
   ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
    }

    ////////////////////////////////// LoadData ////////////////////////////////////////////////////
    public void LoadData() {

        //-------- carga el di de cliente para comprobar  que su perfil este completo
        IDClient =firebaseUser.getUid();


        /////////////////////////////// Oferta /////////////////////////////////////////////////////
        recyclerViewOfertas = (RecyclerView) findViewById(R.id.recyclerrView_ofertas_business);
        recyclerViewOfertas.setLayoutManager(new LinearLayoutManager(this));
        adapterProfileNegociosOfertas = new ArrayList<>();
        adapterRecyclerViewOfertas = new adapter_recyclerView_Ofertas(adapterProfileNegociosOfertas);
        // Onclick
        adapterRecyclerViewOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}});
        recyclerViewOfertas.setAdapter(adapterRecyclerViewOfertas);


        //////////////////////////////// Servicios /////////////////////////////////////////////////
        //Click en el item seleccionado
        recyclerViewServicios = (RecyclerView) findViewById(R.id.recyclerrView_servicios_business);
        recyclerViewServicios.setLayoutManager(new LinearLayoutManager(this));
        adapterProfileNegociosServicios = new ArrayList<>();
        adapterRecyclerViewServicios = new adapter_recyclerView_Servicios(adapterProfileNegociosServicios);
        adapterRecyclerViewServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}});
        recyclerViewServicios.setAdapter(adapterRecyclerViewServicios);


        //------------------------------ Firestore -------------------------------------------------
        if(!IdBusiness.equals("null")){

            DocumentReference docPerfilNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness);
            docPerfilNegocio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        DocumentSnapshot perfil=task.getResult();
                        if(perfil.exists()){
                            adapterProfileNegocio = perfil.toObject(adapter_profile_negocio.class);
                            setTitle(adapterProfileNegocio.getNombre_negocio());


                            //Carga los datos
                            LoadBusinessMakerMaps(IdBusiness);
                            Servicios(IdBusiness);
                            Ofertas(IdBusiness);
                            LoadImageGalery(IdBusiness);
                            LoadReseñas(IdBusiness);
                            notificacionMensajeNuevo(IdBusiness);
                            ComprobarProductosNegocio(IdBusiness);

                            // ToolBar
                            //imagen del negocio
                            if(adapterProfileNegocio.getImagen_perfil().equals("default")){

                                int id= icono.getIconLogoCategoria(adapterProfileNegocio.getCategoria(),MainActivity_lauch_Store.this);
                                imageView_iconStrore.setImageDrawable(getResources().getDrawable(id)); // icono

                                //appBarLayoutL.setBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor())); // color appBar
                                //lTolbar.setBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor())); // color toolBAr

                            }else{
                                //-Carga la imagen de perfil
                                Glide.with(MainActivity_lauch_Store.this).load(adapterProfileNegocio.getImagen_perfil()).into(imageView_iconStrore);
                                }

                                //nombre
                            textView_name_store.setText(adapterProfileNegocio.getNombre_negocio()); //nombre del negocio

                            //profile
                            textViewPais.setText(adapterProfileNegocio.getPais());
                            textViewProvincia.setText(adapterProfileNegocio.getProvincia()+",");
                            textViewCiudad.setText(adapterProfileNegocio.getCiudad()+",");
                            textViewDireccion.setText(adapterProfileNegocio.getDireccion());
                            textViewCodigoPostal.setText(adapterProfileNegocio.getCodigo_postal());
                            // Telefono
                            if(!adapterProfileNegocio.getTelefono().equals("")){
                                textViewTelefono.setText(adapterProfileNegocio.getTelefono());
                                numeroTelefonoBusiness= adapterProfileNegocio.getTelefono();}
                            // Sitio web
                            if(!adapterProfileNegocio.getSitio_web().equals("")){textViewWeb.setText(adapterProfileNegocio.getSitio_web());}


                            // Descripción
                            if(adapterProfileNegocio.getDescripcion().equals("")){
                                cardview_descripcion.setVisibility(View.GONE);
                            }else{
                                textViewDescripcion.setText(adapterProfileNegocio.getDescripcion());
                            }

                            //---------------- Comprobacion que el negocios este en la lista -------------------
                            ComprobacionNegocio();
                        }

                    } else {
                    }

                }
            });
        }else{
            CollectionReference colRefDB= db.collection(  getString(R.string.DB_NEGOCIOS)  );

            GeoPoint geoPoint=new GeoPoint(Latitud,Longitud);

            colRefDB.whereEqualTo("geopoint",geoPoint).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (DocumentSnapshot document : task.getResult()) {
                                    adapterProfileNegocio = document.toObject(adapter_profile_negocio.class);

                                    setTitle(adapterProfileNegocio.getNombre_negocio());

                                    IdBusiness=document.getId();
                                    Servicios(IdBusiness);
                                    Ofertas(IdBusiness);
                                    LoadImageGalery(IdBusiness);
                                    LoadReseñas(IdBusiness);
                                    LoadBusinessMakerMaps(IdBusiness);
                                    notificacionMensajeNuevo(IdBusiness);
                                    ComprobarProductosNegocio(IdBusiness);

                                    //Referencia drawable mediante un string
                                    Context context = imageView_iconStrore.getContext();
                                    int id= icono.getIconLogoCategoria(adapterProfileNegocio.getCategoria(),context);
                                    imageView_iconStrore.setImageDrawable(getResources().getDrawable(id));
                                    textView_name_store.setText(adapterProfileNegocio.getNombre_negocio());
                                    //lTolbar.setBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor())); // color toolBAr

                                    //profile
                                    textViewPais.setText(adapterProfileNegocio.getPais());
                                    textViewProvincia.setText(adapterProfileNegocio.getProvincia()+",");
                                    textViewCiudad.setText(adapterProfileNegocio.getCiudad()+",");
                                    textViewDireccion.setText(adapterProfileNegocio.getDireccion());
                                    textViewCodigoPostal.setText(adapterProfileNegocio.getCodigo_postal());


                                    // Telefono
                                    if(!adapterProfileNegocio.getTelefono().equals("")){
                                        textViewTelefono.setText(adapterProfileNegocio.getTelefono());
                                        numeroTelefonoBusiness= adapterProfileNegocio.getTelefono();}
                                    // Sitio web
                                    if(!adapterProfileNegocio.getSitio_web().equals("")){textViewWeb.setText(adapterProfileNegocio.getSitio_web());}


                                    // Descripción
                                    if(adapterProfileNegocio.getDescripcion().equals("")){
                                        cardview_descripcion.setVisibility(View.GONE);
                                    }else{
                                        textViewDescripcion.setText(adapterProfileNegocio.getDescripcion());
                                    }

                                    //---------------- Comprobacion que el negocios este en la lista -------------------
                                    ComprobacionNegocio();

                                    break;



                                }
                            } else {}
                        }
                    });
        }

    }

    public void Servicios(String id){

        CollectionReference collecRef=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document( id ).collection(  getString(R.string.DB_SERVICIOS)  );
        collecRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                adapterProfileNegociosServicios.removeAll(adapterProfileNegociosServicios);

                for (DocumentSnapshot snapshot3 : documentSnapshots) {
                    //---Carga todos los datos en el adaptador Ofertas
                    adapter_servicios_negocio ContructorItemRecycleviewServicios = snapshot3.toObject(adapter_servicios_negocio.class);

                    //Asigna el icono al adaptador del servicio
                    if(ContructorItemRecycleviewServicios.getTitulo()!=null){
                        cardViewService.setVisibility(View.VISIBLE);
                        adapterProfileNegociosServicios.add(ContructorItemRecycleviewServicios);
                    }
                }
                adapterRecyclerViewServicios.notifyDataSetChanged();

            }
        });

    }
    public void Ofertas(String id){
        CollectionReference collecRef=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_OFERTAS)  );
        collecRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                adapterProfileNegociosOfertas.removeAll(adapterProfileNegociosOfertas);

                for (DocumentSnapshot snapshot3 : documentSnapshots) {
                    cardViewOfert.setVisibility(View.VISIBLE);
                    //---Carga todos los datos en el adaptador Ofertas
                    adapter_ofertas_negocio ContructorItemRecycleviewOferta = snapshot3.toObject(adapter_ofertas_negocio.class);
                    adapterProfileNegociosOfertas.add(ContructorItemRecycleviewOferta);
                    adapterRecyclerViewOfertas.notifyDataSetChanged();
                }
            }
        });
    }
    public void ComprobacionNegocio(){

        DocumentReference DocRef = db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness);
        DocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //---Button
                        buttonAddNegocio.setVisibility(View.GONE);
                        lTolbar.setVisibility(View.VISIBLE);
                        lTolbar.setAnimation(animationUptodown);
                        linearLayout_reseña_calificacion.setVisibility(View.VISIBLE);
                    } else {
                        buttonAddNegocio.setVisibility(View.VISIBLE);
                        buttonAddNegocio.setAnimation(animationUptodown);
                        lTolbar.setVisibility(View.GONE);
                    }
                } else {
                    buttonAddNegocio.setVisibility(View.VISIBLE);
                    buttonAddNegocio.setAnimation(animationUptodown);
                    lTolbar.setVisibility(View.GONE);
                }
            }
        });

    }
    public  void ComprobarProductosNegocio(String ID_NEGOCIO){
        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    button_VerProductos.setVisibility(View.GONE);
                    for (DocumentSnapshot doc:task.getResult()){
                        button_VerProductos.setVisibility(View.VISIBLE);
                        break;
                    }

                }
            }
        });
    }

    public void notificacionMensajeNuevo(String idBusiness){

        DocumentReference documentSnapshot=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(idBusiness);
        documentSnapshot.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();

                    if(doc.exists()){
                        //Notificacion de mensajae nuevo
                        Boolean value=doc.getBoolean(  getString(R.string.DB_mensaje_nuevo)  );
                        if(value!=null){
                            if(value){

                                // notifica que ahi un mensaje nuevo
                                buttonChat.setBackgroundResource(R.mipmap.ic_chat);
                                buttonChat.setBackgroundTintList(getResources().getColorStateList(R.color.color_blaco));
                                animation_new_message.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });


    }


    /////////////////////////////////// Load Galery photo //////////////////////////////////////////
    public void LoadButtonGaleryPhoto(final String id){

        imageViewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefGalery=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto1");
                docRefGalery.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen

                            // Adaptador
                            adaptador_foto adapterNegocioPerfil= documentSnapshot.toObject(adaptador_foto.class);

                            // funcion de viste de la imagen
                            vista_Imagen(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario() );
                        }
                    }
                });

            }});

        imageViewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto2");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            // Adaptador
                            adaptador_foto adapterNegocioPerfil= documentSnapshot.toObject(adaptador_foto.class);

                            // funcion de viste de la imagen
                            vista_Imagen(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario() );
                        }
                    }
                });
            }});
        imageViewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto3");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            // Adaptador
                            adaptador_foto adapterNegocioPerfil= documentSnapshot.toObject(adaptador_foto.class);

                            // funcion de viste de la imagen
                            vista_Imagen(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario() );
                        }
                    }
                });
            }});
        imageViewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto4");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            // Adaptador
                            adaptador_foto adapterNegocioPerfil= documentSnapshot.toObject(adaptador_foto.class);

                            // funcion de viste de la imagen
                            vista_Imagen(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario() );
                        }
                    }
                });
            }});
        imageViewFoto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto5");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            // Adaptador
                            adaptador_foto adapterNegocioPerfil= documentSnapshot.toObject(adaptador_foto.class);

                            // funcion de viste de la imagen
                            vista_Imagen(adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario() );
                        }
                    }
                });
            }});








    }
    public void vista_Imagen(String sUrl, String sComentario){
        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_galery_photo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_lauch_Store.this);
        builder.setView(dialoglayout);
        alertDialogHors=builder.show();

        ImageView imageViewFoto=(ImageView) dialoglayout.findViewById(R.id.imageView_galery_foto);
        final TextView textView_Comentario =(TextView) dialoglayout.findViewById(R.id.textView_comentario);
        ImageView imageView_Cerrar=(ImageButton) dialoglayout.findViewById(R.id.imageButton_close);
        LottieAnimationView anim_loading=(LottieAnimationView) dialoglayout.findViewById(R.id.Anim_loading);

        // Condiciones
        if(sComentario.equals("")){
            textView_Comentario.setVisibility(View.GONE);
        }else{
            textView_Comentario.setText(sComentario);
        }

        try{
            Glide.with(MainActivity_lauch_Store.this)
                    .load(sUrl)
                    .fitCenter()
                    .centerCrop()
                    .into(imageViewFoto);
        }catch (Exception ex){}

        imageView_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cierra la vista
                alertDialogHors.dismiss();
            }
        });
    }
    public void LoadImageGalery(final String id){

        LoadButtonGaleryPhoto(id);

        cardview_Galeria.setVisibility(View.GONE);
        //---Foto 1
        DocumentReference docRefFoto1=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto1");
        docRefFoto1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    cardview_Galeria.setVisibility(View.VISIBLE);
                    imageViewFoto1.setVisibility(View.VISIBLE);
                    try{
                        Glide.with(MainActivity_lauch_Store.this)
                                .load(snapshot.getString("urlfoto"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto1);
                    }catch (Exception ex){}
                }else{
                    cardViewFoto1.setVisibility(View.GONE);
                }

            }});

        //---Foto 2
        DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto2");
        docRefFoto2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    cardview_Galeria.setVisibility(View.VISIBLE);
                    imageViewFoto2.setVisibility(View.VISIBLE);

                    try{
                        Glide.with(MainActivity_lauch_Store.this)
                                .load(snapshot.getString("urlfoto"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto2);
                    }catch (Exception ex){}
                }else{cardViewFoto2.setVisibility(View.GONE);}

            }});

        //---Foto 3
        DocumentReference docRefFoto3=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto3");
        docRefFoto3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    cardview_Galeria.setVisibility(View.VISIBLE);
                    imageViewFoto3.setVisibility(View.VISIBLE);
                    try{
                        Glide.with(MainActivity_lauch_Store.this)
                                .load(snapshot.getString("urlfoto"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto3);
                    }catch (Exception ex){}
                }else{cardViewFoto3.setVisibility(View.GONE);}

            }});

        //---Foto 4
        DocumentReference docRefFoto4=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto4");
        docRefFoto4.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    cardview_Galeria.setVisibility(View.VISIBLE);
                    imageViewFoto4.setVisibility(View.VISIBLE);
                    try{
                        Glide.with(MainActivity_lauch_Store.this)
                                .load(snapshot.getString("urlfoto"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto4);
                    }catch (Exception ex){}
                }else{cardViewFoto4.setVisibility(View.GONE);}

            }});

        //---Foto 5
        DocumentReference docRefFoto5=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(id).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto5");
        docRefFoto5.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    cardview_Galeria.setVisibility(View.VISIBLE);
                    imageViewFoto5.setVisibility(View.VISIBLE);
                    try{
                        Glide.with(MainActivity_lauch_Store.this)
                                .load(snapshot.getString("urlfoto"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto5);
                    }catch (Exception ex){}
                }else{cardViewFoto5.setVisibility(View.GONE);}

            }});



    }

    ///////////////////////////////// RESEÑAS //////////////////////////////////////////////////////
    public void LoadReseñas( String idbusiness) {




        // Reference
        final RecyclerView recyclerViewReseñas;
        final List<adapter_review> adapter_reviews;
        final adapter_recyclerView_Reseñas adapter_recyclerView_reseñas;

        ////////////////////////////////////Adaptador Reseñas //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewReseñas = (RecyclerView) findViewById(R.id.recyclerview_reseñas);
        recyclerViewReseñas.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_reviews = new ArrayList<>();
        adapter_recyclerView_reseñas = new adapter_recyclerView_Reseñas(adapter_reviews,MainActivity_lauch_Store.this);
        recyclerViewReseñas.setAdapter(adapter_recyclerView_reseñas);

        //OnClick
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating==1 || rating==1.5){

                    tRatingbar.setText(getString(R.string.estrella_1));
                    ratingBar.setRating(1);

                    estrellas=1;
                    // habilita el campo para publicar un comentario
                    linearLayout_Edittext_reseña.setVisibility(View.VISIBLE);

                }else if(rating==2 || rating==2.5){
                    tRatingbar.setText(getString(R.string.estrella_2));
                    ratingBar.setRating(2);

                    estrellas=2;
                    // habilita el campo para publicar un comentario
                    linearLayout_Edittext_reseña.setVisibility(View.VISIBLE);

                }else if(rating==3 || rating==3.5){
                    tRatingbar.setText(getString(R.string.estrella_3));
                    ratingBar.setRating(3);

                    estrellas=3;
                    // habilita el campo para publicar un comentario
                    linearLayout_Edittext_reseña.setVisibility(View.VISIBLE);

                }else if(rating==4 || rating==4.5){
                    tRatingbar.setText(getString(R.string.estrella_4));
                    ratingBar.setRating(4);


                    estrellas=4;
                    // habilita el campo para publicar un comentario
                    linearLayout_Edittext_reseña.setVisibility(View.VISIBLE);

                }else if(rating==5 || rating==5.5){
                    tRatingbar.setText(getString(R.string.estrella_5));
                    ratingBar.setRating(5);

                    estrellas=5;
                    // habilita el campo para publicar un comentario
                    linearLayout_Edittext_reseña.setVisibility(View.VISIBLE);

                }

            }
        });





        /////////////////////////////////// RECYCLERVIEW ///////////////////////////////////////////
        adapter_recyclerView_reseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //Extrae de Datos
                // id de reseña
                final String idReseña = adapter_reviews.get(recyclerViewReseñas.getChildAdapterPosition(v)).getIdReseña();
                String id = adapter_reviews.get(recyclerViewReseñas.getChildAdapterPosition(v)).getId();
                //FIREBASE
                final DocumentReference documentReference = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_RESEÑAS)  ).document(idReseña);


                // obtener la fecha y salida por pantalla con formato:
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String datoFechaActual=dateFormat.format(date);
                String datoFechaReseña= dateFormat.format(adapter_reviews.get(recyclerViewReseñas.getChildAdapterPosition(v)).getTimestamp());

                // Alertview
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_lauch_Store.this);
                builder.setTitle(  getString(R.string.DB_RESEÑAS)  );




                // si la id de la reseña es igual que la id del usuario (True)
                if (id.equals(firebaseUser.getUid())) {
                    // 2 Opciones
                    CharSequence opciones1[] = new CharSequence[]{"Editar", "Eliminar"};
                    CharSequence opciones2[] = new CharSequence[]{"Eliminar"};

                    // Si la fecha actual coincide con la fecha de la reseña habilita la opcion de (Editar)
                    if(datoFechaActual.equals(datoFechaReseña)){
                        // Opción 1
                        builder.setItems(opciones1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {

                                    // EDITAR LA RERSEÑA
                                    //Crear AlertDialog Hors
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialoglayout = inflater.inflate(R.layout.view_review_edit, null);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_lauch_Store.this);
                                    builder.setView(dialoglayout);
                                    final AlertDialog alertDialogSen=builder.show();

                                    final EditText editTextReseña=(EditText) dialoglayout.findViewById(R.id.editText_review);
                                    Button buttonActualizarReseña=(Button) dialoglayout.findViewById(R.id.button3_actualizar);

                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc=task.getResult();
                                                if(doc.exists()){
                                                    adapter_review adapterReseñas=doc.toObject(adapter_review.class);
                                                    editTextReseña.setText(adapterReseñas.getReseña());
                                                }
                                            }
                                        }
                                    });

                                    buttonActualizarReseña.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // EDITAR  DE LA RESEÑA

                                            // informacion de la reseña
                                            final Map<String , Object> reseñas=new HashMap<>();
                                            reseñas.put("reseña",editTextReseña.getText().toString());
                                            reseñas.put("timestamp", FieldValue.serverTimestamp());

                                            //FIREBASE
                                            DocumentReference docUpdate = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_RESEÑAS)  ).document(idReseña);
                                            docUpdate.set(reseñas, SetOptions.merge());

                                            alertDialogSen.dismiss();

                                        }
                                    });


                                } else if (item == 1) {

                                    // ELIMINACION  DE LA RESEÑA
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // RESTA CONTEO DE RESEÑAS ECHAS POR EL USUARIO
                                            // Firestore
                                            final DocumentReference AddCountReseña=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
                                            AddCountReseña.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot doc=task.getResult();
                                                        if(doc.exists()){

                                                            // Adapter
                                                            adapter_profile_clientes profileClientes=doc.toObject(adapter_profile_clientes.class);


                                                            if(profileClientes.getNumreseñas() != null){

                                                                double NumReseñas=profileClientes.getNumreseñas();
                                                                NumReseñas -=1;

                                                                // informacion de la reseña contador
                                                                Map<String , Double> Countreseñas=new HashMap<>();
                                                                Countreseñas.put("numreseñas",NumReseñas);

                                                                AddCountReseña.set(Countreseñas, SetOptions.merge());

                                                            }else {

                                                                double NumReseñas=0;

                                                                // informacion de la reseña contador
                                                                Map<String , Double> Countreseñas=new HashMap<>();
                                                                Countreseñas.put("numreseñas",NumReseñas);

                                                                AddCountReseña.set(Countreseñas, SetOptions.merge());

                                                            }

                                                        }
                                                    }

                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });
                        builder.show();

                    }else if(!datoFechaActual.equals(datoFechaReseña)){

                        // Opción 2
                        builder.setItems(opciones2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    DocumentReference documentReference = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_RESEÑAS)  ).document(idReseña);
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Actualiza lista de reseñas
                                            //LoadReseñas(IdBusiness);
                                        }
                                    });
                                }
                            }
                        });

                        builder.show();
                    }

                }else {}

            }
        });

        // Firestore
        CollectionReference AddReseñaNegocio = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(idbusiness).collection(  getString(R.string.DB_RESEÑAS)  );
        AddReseñaNegocio.orderBy(  getString(R.string.DB_timestamp)  , Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {

                tNot_Reseña.setVisibility(View.VISIBLE);
                adapter_reviews.removeAll(adapter_reviews);

                for (DocumentSnapshot doc : querySnapshot) {
                    tNot_Reseña.setVisibility(View.GONE);
                    adapter_review adapterReseñ_a = doc.toObject(adapter_review.class);

                    adapterReseñ_a.setIdReseña(doc.getId());
                    adapter_reviews.add(adapterReseñ_a);


                }
                adapter_recyclerView_reseñas.notifyDataSetChanged();


            }
        });



    }

    //////////////////////////// BUUTTON ///////////////////////////////////////////////////////////
    //Views Floating Hros
    public void ButtonHorarios(View view) {

        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.layout_view_hors, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_lauch_Store.this);
        builder.setView(dialoglayout);
        alertDialogHors = builder.show();

        //Notificacion TextView
        notific_no_hors=(TextView) dialoglayout.findViewById(R.id.textView20);
        notific_no_hors.setVisibility(View.GONE);
        final LottieAnimationView anim_loading=(LottieAnimationView) dialoglayout.findViewById(R.id.Anim_loading);


        ////////////////////////////////////Adaptador Navigation  Horarios //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewHorarios =(RecyclerView) dialoglayout.findViewById(R.id.recyclerView_Horarios_negocio);
        recyclerViewHorarios.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterHorarios =new ArrayList<>();
        adapterRecyclerViewHorarios =new adapter_recyclerView_horario(adapterHorarios);
        recyclerViewHorarios.setAdapter(adapterRecyclerViewHorarios);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference collectionReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_HORARIOS)  );
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    //---vacia la lista  recyclerView
                    adapterHorarios.removeAll(adapterHorarios);

                    for(DocumentSnapshot doc:task.getResult()){
                        notific_no_hors.setVisibility(View.GONE);
                        anim_loading.setVisibility(View.GONE);
                        //---Carga todos los datos en el adaptador de Servicios al adaptador
                        adapter_horario adapterServicios = doc.toObject(adapter_horario.class);
                        adapterHorarios.add(adapterServicios);
                    }
                    adapterRecyclerViewHorarios.notifyDataSetChanged();
                }else{}

                if(adapterHorarios.size() == 0){
                    notific_no_hors.setVisibility(View.VISIBLE);
                    anim_loading.setVisibility(View.VISIBLE);
                }

            }
        });


    }
    public void ButtonHorsOk(View view){
        //Finaliza el alertDialog Hors del negocio
        alertDialogHors.dismiss();
    }
    //Tolbar
    public void Delete_negocio(){


        new AlertDialog.Builder(MainActivity_lauch_Store.this)
                .setIcon(R.mipmap.ic_launcher)
                //.setTitle(R.string.pregunta_eliminar_negocio_de_lista)
                .setMessage(R.string.confirma_eliminacion_negocio)
                .setPositiveButton(R.string.si, null)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Elimina datos del negocio de la cuenta del cliente
                        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //---Finaliza el activity
                                        deleteCollection(db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CHAT)  ), 50, EXECUTOR);
                                        deleteCollection(db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CUENTA)  ), 50, EXECUTOR);

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) { }});

                        db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                deleteCollection(   db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_CHAT)  ), 50, EXECUTOR);

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {}
                }).show();



    }
    public void ButtonAgregarNegocio(View view) {


        //DATA BASE
        DocumentReference docPerfil=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
        docPerfil.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    adapterProfileCliente=documentSnapshot.toObject(adapter_profile_clientes.class);

                    if(adapterProfileCliente.getNombre() != null  && adapterProfileCliente.getId() != null ){
                        if(!adapterProfileCliente.getNombre().equals("")  && !adapterProfileCliente.getId().equals("") ){

                            //---------- DB Cliente
                            Map<String, String> mID_USUARIO = new HashMap<String, String>();
                            mID_USUARIO.put("id",firebaseUser.getUid());
                            //DB Negocio
                            dbNegocio.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid())
                                    .set(mID_USUARIO,SetOptions.merge()) .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    buttonAddNegocio.setVisibility(View.GONE);
                                    lTolbar.setVisibility(View.VISIBLE);
                                    linearLayout_reseña_calificacion.setVisibility(View.VISIBLE);


                                    //---------- DB Cliente
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("id",adapterProfileNegocio.getId());
                                    dbCliente.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).set(map,SetOptions.merge());
                                    map.remove(map);

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                        }else{
                            // Si el usuario no se registro lanza el activity perfil
                            Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, Activity_Profile_Edit.class);
                            startActivity(Lanzador1);
                        }
                    }else{
                        // Si el usuario no se registro lanza el activity perfil
                        Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, Activity_Profile_Edit.class);
                        startActivity(Lanzador1);
                    }

                }else{
                    // Si el usuario no se registro lanza el activity perfil
                    Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, Activity_Profile_Edit.class);
                    startActivity(Lanzador1);
                }
            }
        });



    }
    public void ButtonChat(View view) {

        if(adapterProfileNegocio.getNombre_negocio() != null){
            buttonChat.setBackgroundResource(R.mipmap.ic_chat1);
            animation_new_message.setVisibility(View.GONE);

            //---Lanzador de activity Auth
            Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, Chat_view.class);
            Lanzador1.putExtra("parametroIdClient", IdBusiness);
            startActivity(Lanzador1);
        }


    }
    public void ButtonLlamadaPhone(View view) {

        if(numeroTelefonoBusiness==null){
            Toast.makeText(MainActivity_lauch_Store.this,R.string.telefono_no_disponible,Toast.LENGTH_SHORT).show();
        }else if(numeroTelefonoBusiness.equals("")){
            Toast.makeText(MainActivity_lauch_Store.this,R.string.telefono_no_disponible,Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:"+numeroTelefonoBusiness));

            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 225);
            } else {
                //Log.i("Mensaje", "Se tiene permiso para realizar llamadas!");
                startActivity(i);
            }
        }


    }
    public void ButtoCuenta(View view){

        //---Lanzador de activity Auth
        Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, cuentna_launch.class);
        Lanzador1.putExtra("parametroIdStrore", IdBusiness);
        startActivity(Lanzador1);

    }

    // Reseñas
    public void ButtonPublicar(View view){
        final EditText editTextReseña=(EditText) findViewById(R.id.editText_review);

        if(!editTextReseña.getText().toString().equals("") && estrellas != 0){


            DocumentReference docPerfilUser=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
            docPerfilUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc=task.getResult();
                        if(doc.exists()){
                            adapter_profile_clientes adapterProfileClientes=doc.toObject(adapter_profile_clientes.class);

                            // pone la primera inicial en mayuscula
                            String ValueReseña=editTextReseña.getText().toString();
                            ValueReseña=Character.toUpperCase(ValueReseña.charAt(0)) + ValueReseña.substring(1,ValueReseña.length());


                            // informacion de la reseña
                            final Map<String , Object> reseñas=new HashMap<>();
                            reseñas.put("nombre",adapterProfileClientes.getNombre());
                            reseñas.put("urlfotoPerfil",adapterProfileClientes.getUrlfotoPerfil());
                            reseñas.put("reseña",ValueReseña);
                            reseñas.put("timestamp", FieldValue.serverTimestamp());
                            reseñas.put("id",firebaseUser.getUid());
                            reseñas.put("estrellas",estrellas);


                            // Firestore
                            CollectionReference AddReseñaNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(IdBusiness).collection(  getString(R.string.DB_RESEÑAS)  );
                            AddReseñaNegocio.document().set(reseñas).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Crear AlertDialog Hors
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialoglayout = inflater.inflate(R.layout.view_review_notificacion, null);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_lauch_Store.this);
                                    builder.setView(dialoglayout);
                                    final AlertDialog alertDialogSen=builder.show();

                                    Button buttonOK=(Button) dialoglayout.findViewById(R.id.button_ok);
                                    buttonOK.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            alertDialogSen.dismiss();
                                        }
                                    });

                                    // SUMA CONTEO DE RESEÑAS ECHAS POR EL USUARIO
                                    // Firestore
                                    final DocumentReference AddCountReseña=db.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid());
                                    AddCountReseña.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc=task.getResult();
                                                if(doc.exists()){
                                                    adapter_profile_clientes profileClientes=doc.toObject(adapter_profile_clientes.class);

                                                    if(profileClientes.getNumreseñas() != null){

                                                        double NumReseñas=profileClientes.getNumreseñas();
                                                        NumReseñas +=1;

                                                        // informacion de la reseña contador
                                                        Map<String , Double> Countreseñas=new HashMap<>();
                                                        Countreseñas.put("numreseñas",NumReseñas);

                                                        AddCountReseña.set(Countreseñas, SetOptions.merge());

                                                    }else {

                                                        double NumReseñas=1;

                                                        // informacion de la reseña contador
                                                        Map<String , Double> Countreseñas=new HashMap<>();
                                                        Countreseñas.put("numreseñas",NumReseñas);

                                                        AddCountReseña.set(Countreseñas, SetOptions.merge());

                                                    }

                                                }
                                            }

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}
                            });


                            // Resetea valores
                            ratingBar.setRating(0);
                            editTextReseña.setText("");
                            tRatingbar.setText("");
                            // Desabilita EditText
                            linearLayout_Edittext_reseña.setVisibility(View.GONE);


                        }
                    }
                }
            });
        }

    }

    public void Button_Productos(View view){

        //---Lanzador de activity Auth
        Intent Lanzador1 = new Intent(MainActivity_lauch_Store.this, MainActivity_productos.class);
        Lanzador1.putExtra("ID_NEGOCIO", IdBusiness);
        startActivity(Lanzador1);

    }
    public void Button_GaleriaFotos(View view){
        //--.lanzadador Activity
        Intent intent = new Intent (MainActivity_lauch_Store.this,galeria_fotos.class);
        intent.putExtra("ID_NEGOCIO", IdBusiness);
        startActivityForResult(intent, 0);
    }

    /////////////////////////////////// elimina Colleccion /////////////////////////////////////////
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
    ////////////////////////////////////////// MAPS //////////////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setAllGesturesEnabled(false); //Desactiva los gestos sobre el mapView

        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(20);

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(18.0f);

        LatLng sydney = new LatLng(Latitud, Longitud);
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //LoadBusinessMakerMaps();
    }

    public void LoadBusinessMakerMaps(String ID_NEGOCIOS) {

        DocumentReference docRef = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIOS);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (mMap != null) {
                    mMap.clear();
                }

                if(documentSnapshot.exists()){
                    //---direccion donde se encuentras  los datos de los negocios
                    adapter_profile_negocio ContructorItemRecycleview = documentSnapshot.toObject(adapter_profile_negocio.class);

                    ////////////////// Asigna el icono ssegun la categoria del negocio /////////////
                    int id= icono.getIconLocationCategoria(ContructorItemRecycleview.getCategoria(),MainActivity_lauch_Store.this);
                    BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(id);
                    //---Crea los Makers
                    mMap.addMarker(new MarkerOptions().position(new LatLng(ContructorItemRecycleview.getGeopoint().getLatitude(), ContructorItemRecycleview.getGeopoint().getLongitude()))).setIcon(icon);
                    //Posiciona la camara en la ubicacion del negocio
                    LatLng sydney = new LatLng(ContructorItemRecycleview.getGeopoint().getLatitude(), ContructorItemRecycleview.getGeopoint().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    lottieAnimationView_load.setVisibility(View.GONE);
                }


            }
        });
    }

}
