package com.opencliente.applic.opencliente;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.opencliente.applic.opencliente.BuildConfig;
import com.opencliente.applic.opencliente.MainActivity_Auth;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.MainActivity_interface_principal;

public class SplashScreean extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {



    private String PREFS_KEY = "mispreferencias";


    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;

    //Declaracion de instancia FierebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screean);

        //Ocultar ActionBar
        if(getSupportActionBar() != null) { getSupportActionBar().hide(); }

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
                    goMainScreen();
                }else{
                    RevokeAuth();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);

        try { Thread.sleep (500); } catch (Exception e) { }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }



    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity_interface_principal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.anim_trans2,R.anim.anim_trans1).toBundle();
        ActivityCompat.startActivity(this, intent, options);



    }
    private void RevokeAuth() {
        Intent intent = new Intent(this, MainActivity_Auth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.anim_trans2,R.anim.anim_trans1).toBundle();
        ActivityCompat.startActivity(this, intent, options);

    }


}
