package com.opencliente.applic.opencliente.interface_principal;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.opencliente.applic.opencliente.MainActivity_Auth;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_ofertas_negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_clientes;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_Tarjetas;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_recyclerView_Ofertas;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.OnSwipeTouchListener;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.MainActivity_negocios_Lista_Pedidos;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil.Activity_Profile;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.Chat_principal;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.informacion.informacion_open;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.perfil.Activity_Profile_Edit;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.MainActivity_lauch_Store;
import com.opencliente.applic.opencliente.service.ServiseNotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity_interface_principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    //------------------------ Cloud Firestore -----------------------------------------------------
    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore dbListNegocios = FirebaseFirestore.getInstance();


    //-- Broadcastreceiver
    public  static  final String MENSAJE="MENSAJE";

    private View include_home ;
    private View include_ofertas ;
    private View include_maps;


    //!!!!!!!!!!!!!!!!!!!!! HOME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    protected RecyclerView recyclerViewHome;
    protected List<adapter_profile_negocio> NegociosListArray;
    protected adapter_recyclerView_Tarjetas adapterRecyclerViewHome;

    //---TextView
    public  LinearLayout not_no_internet;
    protected LottieAnimationView animation_view_load;

    private LinearLayout layout_noti_pedido;
    private LinearLayout LinealLayout_ContentFond_Store;
    private CardView toolbar_cardview_seach;
    private  EditText editText_Toolbar_Seach;
    private TextView tToolbsrTituloTarjetas;

    private LottieAnimationView anim_seach;

    ////////////////////////////////////// OFERTAS ///////////////////////////////////////////////
    public RecyclerView recyclerViewOfertas;
    public List<adapter_ofertas_negocio> OfertasListArray;
    public adapter_recyclerView_Ofertas adapterRecyclerViewOfertas;

    //---imagen y texto de fondo si la lista oferta esta vacia
    private LinearLayout LinealLayout_ContentFond_Service;
    private TextView gettToolbsrTituloOfertas;
    //////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////// Nav Perfil ////////////////////////////////////////////////////////////
    private TextView navUsername ;
    private TextView navEmail ;
    private  TextView navCounReseñas;
    private CircleImageView navImgUser;
    private String url_play_store;





    ////////////////////////////////// Google Maps /////////////////////////////////////////////////
    public GoogleMap mGoogleMap;
    public SupportMapFragment mapFrag;
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    public Marker mCurrLocationMarker;
    private View popputMaps;

    private Context contextGeocode;
    public String LocationCountryGps;
    private String stringValueBuscador=" ";

    // SEACH
    //Reference
    private EditText editTextSeach;
    // RecyclerView Seach
    public RecyclerView rvSeach;
    public List<adapter_profile_negocio> adapterProfileNegociosSeach;
    public adapter_recyclerView_Tarjetas adapterRecyclerViewSeach;

    //-Storage
    private StorageReference mStorage;

    //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //-------- signOut
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //#################################################################################################
    //############################## Button Navegation ################################################

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    /////////////////////////////////  CONECTIVIDAD  A INTERNET  ///////////////////////////////////
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    //###################################################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface_principal);
        setTitle(R.string.app_name);


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


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                }else{
                   // RevokeAuth();
                }
            }
        };

        ////////////////////////////////////////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //------------------  carga los datos en el navigation -----------------------------
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        navUsername = (TextView) hview.findViewById(R.id.nav_id_name);
        navEmail = (TextView) hview.findViewById(R.id.nav_email);
        navCounReseñas=(TextView) hview.findViewById(R.id.textView_contador);
        navImgUser=(CircleImageView) hview.findViewById(R.id.nav_photoImageView);



        //------------------------------------------------------------------------------------

        //############################## Button Navegation #########################################
        //---Reference
        //include
        include_home = (View) findViewById(R.id.include_home);
        include_ofertas = (View) findViewById(R.id.include_ofertas);
        include_maps = (View) findViewById(R.id.include_maps);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();
        //---Fragment
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.navigation_home:
                        include_home.setVisibility(View.VISIBLE);
                        include_ofertas.setVisibility(View.GONE);
                        include_maps.setVisibility(View.GONE);
                        toolbar_cardview_seach.setVisibility(View.VISIBLE);


                        if(  !tToolbsrTituloTarjetas.getText().toString().equals(getResources().getString(R.string.tus_tarjetas)) ){

                            // Titulo Toolbar
                            tToolbsrTituloTarjetas.setText(getResources().getString(R.string.tus_tarjetas));

                            // Carga la lista de tarjetas
                            Carga_Recyclerview_tarjetas();

                        }

                        break;
                    case R.id.navigation_ofertas:
                        include_home.setVisibility(View.GONE);
                        include_ofertas.setVisibility(View.VISIBLE);
                        include_maps.setVisibility(View.GONE);
                        toolbar_cardview_seach.setVisibility(View.GONE);


                        break;
                    case R.id.navigation_maps:
                        include_home.setVisibility(View.GONE);
                        include_ofertas.setVisibility(View.GONE);
                        include_maps.setVisibility(View.VISIBLE);
                        toolbar_cardview_seach.setVisibility(View.GONE);

                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        if(!gps){ showAlert(); }
                        break;
                }return true;}});
        //##########################################################################################


        //////////////////////////////////// HOME //////////////////////////////////////////////////

        //------------------------------ Touch event update-----------------------------------------------
        RecyclerView recyclerView_home=(RecyclerView) findViewById(R.id.recyclerView_home);
        recyclerView_home.setOnTouchListener(new OnSwipeTouchListener(MainActivity_interface_principal.this) {
            public void onSwipeBottom() { }});
        //------------------------------------------------------------------------------------------

        //Reference
        layout_noti_pedido=(LinearLayout) findViewById(R.id.layout_noti_pedido);
        layout_noti_pedido.setVisibility(View.GONE);
        LinealLayout_ContentFond_Store=(LinearLayout) findViewById(R.id.LinealLayout_ContentFond_Store);
        tToolbsrTituloTarjetas =(TextView) findViewById(R.id.textView14);
        not_no_internet=(LinearLayout) findViewById(R.id.not_no_internet);
        animation_view_load=(LottieAnimationView) findViewById(R.id.animation_view_load);
        toolbar_cardview_seach =(CardView) findViewById(R.id.cardview_seach);
        editText_Toolbar_Seach=(EditText) findViewById(R.id.editText2_seach);
        anim_seach=(LottieAnimationView) findViewById(R.id.anim_seach);
        anim_seach.pauseAnimation();


        editText_Toolbar_Seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(!editText_Toolbar_Seach.getText().toString().equals("")){
                    anim_seach.setVisibility(View.VISIBLE);
                    anim_seach.playAnimation();
                    tToolbsrTituloTarjetas.setText(getResources().getString(R.string.resultado));
                    LinealLayout_ContentFond_Store.setVisibility(View.GONE);

                    // Buscador
                    Toolbar_Seach(editText_Toolbar_Seach.getText().toString());
                }else {
                    anim_seach.setVisibility(View.GONE);
                    anim_seach.pauseAnimation();

                    tToolbsrTituloTarjetas.setText(getResources().getString(R.string.tus_tarjetas));
                    Carga_Recyclerview_tarjetas();
                }
            }
        });

        /////////////////////////////// OFERTA /////////////////////////////////////////////////////
        //------------------------------ Touch event -----------------------------------------------

        RecyclerView recyclerView_oferta=(RecyclerView) findViewById(R.id.recyclerView_oferta);
        recyclerView_oferta.setOnTouchListener(new OnSwipeTouchListener(MainActivity_interface_principal.this) {
            public void onSwipeBottom() { }});

        //---Reference
        LinealLayout_ContentFond_Service=(LinearLayout) findViewById(R.id.textView_notificacion_oferta);
        gettToolbsrTituloOfertas=(TextView) findViewById(R.id.textView_ofertas);


        /////////////////////////////////// SEACH //////////////////////////////////////////////////
        // Refence
        editTextSeach=(EditText) findViewById(R.id.editText_buscador);

        ///////////////////////////////////// Google Maps //////////////////////////////////////////
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);

        //-- Broadcastreceiver
        startService(new Intent(this,ServiseNotify.class));

        /////////////////////////// CARGA DATOS ////////////////////////////////////////////////////

        // LISTA DE TARJETAS
        Carga_Recyclerview_tarjetas();
        Cargar_pedidos();

    }


    ///////////////////////////////////// Ciclo de vida app ////////////////////////////////////////
    @Override
    public void onBackPressed() { moveTaskToBack(true); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_activity_interface_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            /////////////////////////////////// Perfil /////////////////////////////////////////////
            //Lanzador
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,Activity_Profile.class);
            startActivity(Lanzador1);

        } else if (id == R.id.nav_chat) {
            /////////////////////////////////// Chat ///////////////////////////////////////////////
            //Lanzador
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,Chat_principal.class);
            startActivity(Lanzador1);

        } else if (id == R.id.nav_cerrar_sesion) {

            ///////////////////////////////// Cerrar Sesion //////////////////////////////////////////////
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {

                        stopService(new Intent(MainActivity_interface_principal.this,ServiseNotify.class));

                        //---Lanzador de activity Auth
                        Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_Auth.class);
                        startActivity(Lanzador1);
                        finish();
                    } else {Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();}}});

        }else if (id == R.id.nav_compartir) {

            if(url_play_store != null && !url_play_store.equals("")){

                //------------------------ Comparti App ------------------------------------------------
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url_play_store);
                startActivity(Intent.createChooser(intent, "Share with"));

                try{ startActivity(intent); }
                catch(Exception e){ intent.setData(Uri.parse(url_play_store));

                }
            }else{Toast.makeText(MainActivity_interface_principal.this,R.string.error_obtener_link_playstore,Toast.LENGTH_SHORT).show();}



        } else if (id == R.id.nav_informacion) {

            ///////////////////////////////  informacion ///////////////////////////////////////////
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,informacion_open.class);
            startActivity(Lanzador1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        // DATOS DEL USUARIO
        nav_load_profile();
    }
    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();


        //stop location updates when Activity is no longer active
        //if (mGoogleApiClient != null) {
        //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //}
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    //------------------------ comprobacion de conexion a internet ---------------------------------
    private void onNetworkChange(NetworkInfo networkInfo) {

        try {
            if (networkInfo != null) {

                if (networkInfo.getState() == NetworkInfo.State.CONNECTED && networkInfo.isAvailable() && networkInfo.isConnected()){


                    // Conexion a internet
                    not_no_internet.setVisibility(View.GONE);

                } else {
                    //Sin conexion a intnernet
                    not_no_internet.setVisibility(View.VISIBLE);

                }
            }else{
                //Sin conexion a intnernet
                not_no_internet.setVisibility(View.VISIBLE);

            }
        }catch (Exception ex){ Toast.makeText(MainActivity_interface_principal.this,"Error conectividad",Toast.LENGTH_LONG).show();}

    }

    //////////////////////////////// CARGA TODOS LOS DATOS /////////////////////////////////////////
    public void nav_load_profile(){

        // E-mail del usuario
        navEmail.setText(firebaseUser.getEmail());

        // Base de Datos
        final DocumentReference docProfile=db.collection(  getString(R.string.DB_CLIENTES)  ).document( firebaseUser.getUid() );
        docProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(firebaseAuth != null){
                    if(task.isSuccessful()){
                        DocumentSnapshot Docperfil=task.getResult();
                        if(Docperfil.exists()){
                            //Adaptador perfil cliente
                            adapter_profile_clientes PerfilCliente=Docperfil.toObject(adapter_profile_clientes.class);

                            if(PerfilCliente.getNombre() != null){

                                // Foto de perfil
                                if(PerfilCliente.getUrlfotoPerfil() != null){
                                    //-Descarga la foto perfil client
                                    Glide.clear(navImgUser);
                                    Glide.with(MainActivity_interface_principal.this)
                                            .load(PerfilCliente.getUrlfotoPerfil())
                                            .fitCenter()
                                            .centerCrop()
                                            .into(navImgUser);
                                }

                                // Nombre
                                navUsername.setText(PerfilCliente.getNombre());

                                // Numero de Publicaciones
                                if(PerfilCliente.getNumreseñas() != null){
                                    navCounReseñas.setText(String.valueOf( (int) PerfilCliente.getNumreseñas().doubleValue()));
                                }

                            }else {
                                Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, Activity_Profile_Edit.class);
                                startActivity(Lanzador1);
                            }
                        }else{
                            Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, Activity_Profile_Edit.class);
                            startActivity(Lanzador1);
                        }
                    }

                }else{
                    Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, MainActivity_Auth.class);
                    startActivity(Lanzador1);
                }

            }

        });

        // DB Url Play Store
        DocumentReference docUrl=db.collection(  getString(R.string.DB_APP)  ).document(  getString(R.string.DB_INFORMACION)  );
        docUrl.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        url_play_store=doc.getString("url_open_cliente");
                    }
                }
            }
        });


    }
    public void Carga_Recyclerview_tarjetas() {
        ////////////////////////// RECYCLERVIEW OFERTAS ////////////////////////////////////////////
        recyclerViewOfertas = (RecyclerView) findViewById(R.id.recyclerView_oferta);
        recyclerViewOfertas.setLayoutManager(new LinearLayoutManager(this));
        OfertasListArray = new ArrayList<>();
        adapterRecyclerViewOfertas = new adapter_recyclerView_Ofertas(OfertasListArray);
        adapterRecyclerViewOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, MainActivity_lauch_Store.class);
                Lanzador1.putExtra("parametroId", OfertasListArray.get(recyclerViewOfertas.getChildAdapterPosition(view)).getId_negocio());
                startActivity(Lanzador1);

            }
        });
        recyclerViewOfertas.setAdapter(adapterRecyclerViewOfertas);


        ///////////////////////////// RECYCLERVIEW HOME ////////////////////////////////////////////
        recyclerViewHome = (RecyclerView) findViewById(R.id.recyclerView_home);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this,3));
        NegociosListArray = new ArrayList<>();
        adapterRecyclerViewHome = new adapter_recyclerView_Tarjetas(NegociosListArray,this);
        adapterRecyclerViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, MainActivity_lauch_Store.class);
                Lanzador1.putExtra("parametroId", NegociosListArray.get(recyclerViewHome.getChildAdapterPosition(view)).getId());
                startActivity(Lanzador1);
            }
        });
        recyclerViewHome.setAdapter(adapterRecyclerViewHome);

        //************************** Creacion del recycleView **************************************
        dbListNegocios.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_NEGOCIOS)  )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot task,@Nullable FirebaseFirestoreException e) {

                        NegociosListArray.removeAll(NegociosListArray);
                        OfertasListArray.removeAll(OfertasListArray);

                        //Control de Visibilidad de Contenido
                        ControlVisivilityContent("lista_negocio_vacia"); //store
                        animation_view_load.setVisibility(View.GONE);       //Desabilita progressBar
                        ControlVisivilityContent("ListOfertEmpty_Visibility");//Service

                        for (DocumentSnapshot doc : task) {
                            if (doc.exists()){
                                // Set
                                tToolbsrTituloTarjetas.setText(R.string.tus_tarjetas);

                                //Control de Visibilidad de Contenido
                                ControlVisivilityContent("ListStoreEmpty_Gone");
                                //constructor  perfil del negocio
                                adapter_profile_negocio AdaptadorItemPerlfilNegocio=doc.toObject(adapter_profile_negocio.class);

                                //--Asigna el adaptador al recyclerView
                                NegociosListArray.add(AdaptadorItemPerlfilNegocio);//Agrega perfil del negocio al RecyclerView
                                adapterRecyclerViewHome.notifyDataSetChanged();

                                carga_Ofertas(doc.getId());
                            }

                        }
                    }
                });
    }
    private void carga_Ofertas(final String ID_negocio) {

        ControlVisivilityContent("lista_ofertas_vacia");

        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_negocio).collection(  getString(R.string.DB_OFERTAS)  )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                 for(DocumentSnapshot documentSnapshot:task.getResult()){
                     //Control de Visibilidad de Contenido Oferta
                     ControlVisivilityContent("ofertas");

                     //Adaptador del metadatos de ofertas
                     adapter_ofertas_negocio AdaptadorOfertasNegocios=documentSnapshot.toObject(adapter_ofertas_negocio.class);
                     AdaptadorOfertasNegocios.setId_negocio(ID_negocio);

                     OfertasListArray.add(AdaptadorOfertasNegocios);//Agrega la oferta al RecyclerView
                 }

                    adapterRecyclerViewOfertas.notifyDataSetChanged();

                } else { }
            }
        });



    }
    private void Cargar_pedidos(){

        // Firesote
        CollectionReference collectionReference=dbListNegocios.collection(  getString(R.string.DB_CLIENTES)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_PEDIDOS)  );
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot task,@Nullable FirebaseFirestoreException e) {
                        // Control de visivilidad
                        layout_noti_pedido.setVisibility(View.GONE);

                        for (DocumentSnapshot doc : task) {
                            if (doc.exists()) {

                                // Control de visivilidad
                                layout_noti_pedido.setVisibility(View.VISIBLE);
                                break;
                            }

                        }

                    }
                });

        // onClick
        layout_noti_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_negocios_Lista_Pedidos.class);
                startActivity(Lanzador1);
            }
        });

    }

    public  void ControlVisivilityContent(String Content){

        //Control de Visibilidad de Contenido
        if(Content.equals("lista_negocio_vacia")){

            ControlVisivilityContent("lista_ofertas_vacia");

            //---imagen y texto de fondo si la lista de Negocios esta vacia
            LinealLayout_ContentFond_Store.setVisibility(View.VISIBLE);
            tToolbsrTituloTarjetas.setText(R.string.aun_no_tienes_ninguna_tarjeta_negocio);
            tToolbsrTituloTarjetas.setTextSize(14);

            //---Anim load
            animation_view_load.setVisibility(View.GONE);


        }else if(Content.equals("ListStoreEmpty_Gone")){
            //---imagen y texto de fondo si la lista de Negocios esta vacia
            LinealLayout_ContentFond_Store.setVisibility(View.GONE);
            tToolbsrTituloTarjetas.setTextSize(24);
            //---Anim load
            animation_view_load.setVisibility(View.GONE);



        }else if(Content.equals("lista_ofertas_vacia")){

            //---imagen y texto de fondo si la lista oferta esta vacia
            LinealLayout_ContentFond_Service.setVisibility(View.VISIBLE);
            //---Anim load
            animation_view_load.setVisibility(View.GONE);

            // Titulo
            gettToolbsrTituloOfertas.setText(R.string.no_tienes_ninguna_oferta);
            gettToolbsrTituloOfertas.setTextSize(15);

        }else if(Content.equals("ofertas")){

            //---imagen y texto de fondo si la lista oferta contiene ofertas
            LinealLayout_ContentFond_Service.setVisibility(View.GONE);
            //---Anim
            animation_view_load.setVisibility(View.GONE);

            // Titulo
            gettToolbsrTituloOfertas.setText(R.string.ofertas);
            gettToolbsrTituloOfertas.setTextSize(24);

        }else if(Content.equals("internet")){



        }else{}
    }
    public void Toolbar_Seach(final String valueSeach){


        CollectionReference collectNegocios=db.collection(  getString(R.string.DB_NEGOCIOS)  );
        collectNegocios.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    NegociosListArray.removeAll(NegociosListArray);

                    for(DocumentSnapshot doc:task.getResult()){

                        //---Carga todos los datos en el adaptador
                        adapter_profile_negocio ContructorItemRecycleview=doc.toObject(adapter_profile_negocio.class);

                        //--- Condicional de de metodo de busqueda
                        if(seach(ContructorItemRecycleview.getNombre_negocio(),valueSeach)
                                || seach(ContructorItemRecycleview.getCategoria(),valueSeach)){

                            // Finaliza la animacion de busqueda
                            anim_seach.setVisibility(View.GONE);
                            anim_seach.pauseAnimation();
                            tToolbsrTituloTarjetas.setText(getResources().getString(R.string.resultado));

                            //---carga los datos al adaptador del recycleView
                            NegociosListArray.add(ContructorItemRecycleview);

                        }else {
                            if(NegociosListArray.size()==0){

                                // Finaliza la animacion de busqueda
                                anim_seach.setVisibility(View.GONE);
                                anim_seach.pauseAnimation();
                                tToolbsrTituloTarjetas.setText(getResources().getString(R.string.sin_resultado));

                            }
                        }
                    }
                    adapterRecyclerViewHome.notifyDataSetChanged();
                }
            }
        });

    }


    //################################# Maps #######################################################
    // Carga los puntos de coordenadas de los negocios
    public void ButtonBuscador(View view){


        stringValueBuscador=editTextSeach.getText().toString();

        if(!stringValueBuscador.equals("")){



            //////////////////////////////// Cuadro de Dialog //////////////////////////////////


            final Dialog dialoglayout = new Dialog(MainActivity_interface_principal.this);
            dialoglayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialoglayout.setCancelable(true);
            dialoglayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialoglayout.setContentView(R.layout.view_list_seach);
            dialoglayout.show();

            // Reference
            final TextView textViewNotSinResult=(TextView) dialoglayout.findViewById(R.id.not_sin_resultado);

            //////////////////////////////////// AlertDialog Clientes //////////////////////////////////////////////////////
            rvSeach=(RecyclerView) dialoglayout.findViewById(R.id.recyclerview_seach);
            rvSeach.setLayoutManager(new GridLayoutManager(this,3));
            final ProgressBar progressBarSeach=(ProgressBar) dialoglayout.findViewById(R.id.progressBar_seach);

            adapterProfileNegociosSeach =new ArrayList<>();
            adapterRecyclerViewHome =new adapter_recyclerView_Tarjetas(adapterProfileNegociosSeach,this);
            adapterRecyclerViewHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_lauch_Store.class);
                    Lanzador1.putExtra("parametroId",adapterProfileNegociosSeach.get(rvSeach.getChildAdapterPosition(view)).getId());
                    Lanzador1.putExtra("parametroLatitud", adapterProfileNegociosSeach.get(rvSeach.getChildAdapterPosition(view)).getGeopoint().getLatitude());
                    Lanzador1.putExtra("parametroLongitud", adapterProfileNegociosSeach.get(rvSeach.getChildAdapterPosition(view)).getGeopoint().getLongitude());

                    startActivity(Lanzador1);

                }});
            rvSeach.setAdapter(adapterRecyclerViewHome);


            // notifica Si la geolocalización esta desactivada
            if(LocationCountryGps == null){
                textViewNotSinResult.setText(R.string.ubicacion_no_disponible_info);
                textViewNotSinResult.setVisibility(View.VISIBLE);
                progressBarSeach.setVisibility(View.GONE);
            }else {

                // Consulta en la base de datos
                CollectionReference collectNegocios=db.collection(  getString(R.string.DB_NEGOCIOS)  );
                collectNegocios.whereEqualTo("provincia",LocationCountryGps).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        adapterProfileNegociosSeach.removeAll(adapterProfileNegociosSeach);
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc:task.getResult()){
                                //---Carga todos los datos en el adaptador
                                adapter_profile_negocio ContructorItemRecycleview=doc.toObject(adapter_profile_negocio.class);

                                //--- Condicional de de metodo de busqueda
                                if(seach(ContructorItemRecycleview.getNombre_negocio(),stringValueBuscador) || seach(ContructorItemRecycleview.getCategoria(),stringValueBuscador)){

                                    textViewNotSinResult.setVisibility(View.GONE);
                                    progressBarSeach.setVisibility(View.GONE);

                                    // Concatenacion de la ciudad y la direccion del negocio
                                    ContructorItemRecycleview.setDireccion(ContructorItemRecycleview.getCiudad()+", "+ContructorItemRecycleview.getDireccion());

                                    //---carga los datos al adaptador del recycleView
                                    adapterProfileNegociosSeach.add(ContructorItemRecycleview);

                                }else {
                                    if(adapterProfileNegociosSeach.size()==0){

                                        //notificacion que no se encontro nada
                                        textViewNotSinResult.setVisibility(View.VISIBLE);
                                        progressBarSeach.setVisibility(View.GONE);
                                    }
                                }
                            }
                            adapterRecyclerViewHome.notifyDataSetChanged();
                        }
                    }
                });
            }




        }



    }

    //---Algoritmo de busqueda
    private boolean seach(String value,String valueSeach){

        boolean resultado = false;
        if(value != null){
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

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity_interface_principal.this);
        dialog.setTitle(R.string.ubicacion_desactivada)
                .setMessage(R.string.ubicacion_no_disponible_info)
                .setPositiveButton(R.string.config_ubicacion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();


    }

    //---Carga datos de los negocios en el mapa
    public void LoadBusinessMakerMaps( String sProvincia){
        /////////////////////////////////Firebase Database////////////////////////////////////////////
        //---Carga todas las ubicaciones  de los negocios desde la database firebase

        db.collection(  getString(R.string.DB_NEGOCIOS)  )
                .whereEqualTo("provincia",sProvincia)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(mGoogleMap!=null){mGoogleMap.clear();}

                            for(DocumentSnapshot documentSnapshot:task.getResult()){

                                //---direccion donde se encuentras  los datos de los negocios
                                adapter_profile_negocio ContructorItemRecycleview=documentSnapshot.toObject(adapter_profile_negocio.class);

                                    ////////////////// Asigna el icono ssegun la categoria del negocio /////////////
                                    int id= icono.getIconLocationCategoria(ContructorItemRecycleview.getCategoria(),MainActivity_interface_principal.this);
                                    BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(id); //convert bitmap


                                    //Localización de de la ciudad
                                    try {
                                    Geocoder geo = new Geocoder(contextGeocode);
                                    List<Address> addresses = geo.getFromLocation(ContructorItemRecycleview.getGeopoint().getLatitude(), ContructorItemRecycleview.getGeopoint().getLongitude(), 1);
                                    List<Address> addressesGps = geo.getFromLocation(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude(), 1);
                                    if (addresses.isEmpty()) {
                                    }
                                    else {if (addresses.size() > 0) {

                                        // Condicion carga todos los mark del estado/provincia
                                        if(addressesGps.get(0).getAdminArea().equals(addresses.get(0).getAdminArea())){
                                            //---Crea los Makers
                                            mGoogleMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(ContructorItemRecycleview.getGeopoint().getLatitude(),ContructorItemRecycleview.getGeopoint().getLongitude()))
                                                    .title(ContructorItemRecycleview.getNombre_negocio())
                                                    .snippet(ContructorItemRecycleview.getCiudad()+" ("+ContructorItemRecycleview.getDireccion()+")")).setIcon(icon);
                                        }}}}
                                catch (Exception e) {e.printStackTrace(); }




                            }
                        }

                    }
                });

        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////
    }

    //---Crea el mapa
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mGoogleMap=googleMap;
        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //mGoogleMap.setMinZoomPreference(12.0f);
        //mGoogleMap.setMaxZoomPreference(100.0f);

        //---Establece el tipo de mapa de Google
        // Personaliza el estilo del mapa base usando un objeto JSON definido
        // en un archivo de recursos de cadena. Primero crea un objeto MapStyleOptions
        // desde la cadena de estilos JSON, luego pasa esto al setMapStyle
        // método del objeto GoogleMap.
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_maps));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();}}
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);}



        mGoogleMap.setOnInfoWindowClickListener(this);

    }
    //---onClickListener de los Makers
    @Override
    public void onInfoWindowClick(Marker marker) {

        if(!navUsername.getText().equals("")){
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_lauch_Store.class);
            Lanzador1.putExtra("parametroLatitud",marker.getPosition().latitude);
            Lanzador1.putExtra("parametroLongitud",marker.getPosition().longitude);
            Lanzador1.putExtra("parametroId","null");
            startActivity(Lanzador1);
        }else{
            Intent Lanzador1 = new Intent(MainActivity_interface_principal.this, Activity_Profile.class);
            startActivity(Lanzador1);
        }


    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}

    //--- Crea makers de la ubicacion del negocio
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Como viste en la sección anterior, un Marker es un objeto interactivo que marca una ubicación específica
        // en la superficie del mapa, para representar un lugar, establecimientos, objetos, etc.

        //---Camara se posiciona en la localizacion
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        //Localización de de la ciudad
        try {
            contextGeocode=this.getApplicationContext();
            TextView textView=(TextView) findViewById(R.id.textView8);
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.isEmpty()) {
                textView.setText(getResources().getString(R.string.esperando_ubicacion));}
            else {
                if (addresses.size() > 0) {
                    textView.setText(addresses.get(0).getLocality()); // localcidad
                    LocationCountryGps=addresses.get(0).getLocality();

                    LocationCountryGps=addresses.get(0).getAdminArea().toUpperCase(); // provincia
                    editTextSeach.setHint(getResources().getString(R.string.buscar)+" en "+LocationCountryGps);

                    //Carga los marker de la zona de la ubicación
                    LoadBusinessMakerMaps(LocationCountryGps);
               }}
        }
        catch (Exception e) {e.printStackTrace(); }

        float zoom=17.0f;// valor del zoom del punto de locacion del gps

        //mueve la camara automaticamente en el punto de gps
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        //-Carga los datos en el mapaa


    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity_interface_principal.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public View getInfoWindow(Marker marker) { return null; }
    @Override
    public View getInfoContents(Marker marker) {
        popputMaps =getLayoutInflater().inflate(R.layout.view_popupmaps_info,null);
        TextView textViewTitle = (TextView) popputMaps.findViewById(R.id.popp_title);
        TextView textViewInfo = (TextView) popputMaps.findViewById(R.id.popp_info);

        textViewTitle.setText(marker.getTitle());
        textViewInfo.setText(marker.getSnippet());
        return popputMaps;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}
