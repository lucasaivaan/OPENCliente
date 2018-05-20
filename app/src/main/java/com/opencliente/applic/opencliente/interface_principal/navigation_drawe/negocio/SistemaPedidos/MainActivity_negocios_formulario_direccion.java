package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores.adaptador_direccion;

public class MainActivity_negocios_formulario_direccion extends AppCompatActivity {

    // EDITTERXT
    private EditText editText_calle;
    private EditText editText_numero;
    private EditText editText_piso_depto;
    private EditText editText_entre_calles;
    private EditText editText_ciudad;
    private EditText editText_localidad;


    // String datos
    private String sCalle;
    private String sNumero;
    private String sPisoDepto;
    private String sEntreCalles;
    private String sCiudad;
    private String sLocalidad;



    // FIREBASE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_negocios_formulario_direccion);
        setTitle(getResources().getString(R.string.añadir_direccion));


        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Refences
        editText_calle=(EditText) findViewById(R.id.input_calle);
        editText_numero=(EditText) findViewById(R.id.input_numero);
        editText_piso_depto=(EditText) findViewById(R.id.input_piso_departamento);
        editText_entre_calles=(EditText) findViewById(R.id.input_entre_calle);
        editText_ciudad=(EditText) findViewById(R.id.input_ciudad);
        editText_localidad=(EditText) findViewById(R.id.input_localidad);

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


    public void Button_Continuar(View view){


        sCalle=editText_calle.getText().toString();
        sNumero=editText_numero.getText().toString();
        sPisoDepto=editText_piso_depto.getText().toString();
        sEntreCalles=editText_entre_calles.getText().toString();
        sCiudad=editText_ciudad.getText().toString();
        sLocalidad=editText_localidad.getText().toString();

        if( !sCalle.equals("") && !sNumero.equals("") && !sEntreCalles.equals("")
                && !sCiudad.equals("") && !sLocalidad.equals("") ){

            // Adaptador de direccion
            adaptador_direccion adaptadorDireccion=new adaptador_direccion();
            adaptadorDireccion.setCalle(sCalle);
            adaptadorDireccion.setNumero(sNumero);
            adaptadorDireccion.setPiso_depto(sPisoDepto);
            adaptadorDireccion.setEntre_calles(sEntreCalles);
            adaptadorDireccion.setCiudad(sCiudad);
            adaptadorDireccion.setLocalidad(sLocalidad);

            // Firestore
            DocumentReference documentReference=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document( firebaseAuth.getUid() ).collection( getResources().getString(R.string.DB_DIRECCIONES) ).document();
            // Set
            documentReference.set(adaptadorDireccion, SetOptions.merge());

            // Finish activity
            finish();

        }else{
            Toast.makeText(MainActivity_negocios_formulario_direccion.this,getResources().getString(R.string.noti_complete_los_campos_necesarios),Toast.LENGTH_LONG).show();
        }

    }
}
