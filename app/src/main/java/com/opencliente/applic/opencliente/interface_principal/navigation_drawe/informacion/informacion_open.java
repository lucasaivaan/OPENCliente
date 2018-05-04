package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.informacion;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;

public class informacion_open extends AppCompatActivity {

    // Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    // String
    private String sInformacion;

    // TextView
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_informacion_open);


        //Reference
        textViewInfo=(TextView) findViewById(R.id.textView5);
        DocumentReference documentSnapshot=db.collection(  getString(R.string.DB_APP)  ).document(  getString(R.string.DB_INFORMACION)  );


        // Consulta informacion del app
        documentSnapshot.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        if(doc.getString("informacion_open_cliente") != null){
                            sInformacion=doc.getString("informacion_open_cliente");
                            textViewInfo.setText(sInformacion);
                        }
                    }
                }
            }
        });
    }

    public void ButtonOK(View view){
        finish();
    }
}
