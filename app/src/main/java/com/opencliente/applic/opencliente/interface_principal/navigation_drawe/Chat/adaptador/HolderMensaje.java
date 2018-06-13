package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opencliente.applic.opencliente.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 30/10/2017.
 */

public class HolderMensaje extends RecyclerView.ViewHolder{

    //-- Receptor (1)
    private TextView mensaje_1;
    private TextView hora_1;
    private ImageView fotoMensaje_1;
    private RelativeLayout linearLayoutReceptor_1;


    //-- Emisor (2)
    private TextView mensaje_2;
    private TextView hora_2;
    private ImageView fotoMensaje_2;
    private RelativeLayout linearLayoutEmisor_2;



    public HolderMensaje(View itemView) {
        super(itemView);
        // Receptor
        mensaje_1 = (TextView) itemView.findViewById(R.id.mensajeMensaje_1);
        hora_1 = (TextView) itemView.findViewById(R.id.horaMensaje_1);
      linearLayoutReceptor_1=(RelativeLayout) itemView.findViewById(R.id.linealLayout_receptor);

        // Emisor
        mensaje_2 = (TextView) itemView.findViewById(R.id.mensajeMensaje_2);
        hora_2 = (TextView) itemView.findViewById(R.id.horaMensaje_2);
        linearLayoutEmisor_2=(RelativeLayout) itemView.findViewById(R.id.linealLayout_emisor);

    }


    // Set
    public void setMensaje_1(TextView mensaje_1) {this.mensaje_1 = mensaje_1;}
    public void setHora_1(TextView hora_1) {this.hora_1 = hora_1;}
    public void setFotoMensaje_1(ImageView fotoMensaje_1) {this.fotoMensaje_1 = fotoMensaje_1;}
    public void setLinearLayoutReceptor_1(RelativeLayout linearLayoutReceptor_1) {this.linearLayoutReceptor_1 = linearLayoutReceptor_1;}
    public void setMensaje_2(TextView mensaje_2) {this.mensaje_2 = mensaje_2;}
    public void setHora_2(TextView hora_2) {this.hora_2 = hora_2;}
    public void setFotoMensaje_2(ImageView fotoMensaje_2) {this.fotoMensaje_2 = fotoMensaje_2;}
    public void setLinearLayoutEmisor_2(RelativeLayout linearLayoutEmisor_2) {this.linearLayoutEmisor_2 = linearLayoutEmisor_2;}

    // Get
    public TextView getMensaje_1() {return mensaje_1;}
    public TextView getHora_1() {return hora_1;}
    public ImageView getFotoMensaje_1() {return fotoMensaje_1;}
    public RelativeLayout getLinearLayoutReceptor_1() {return linearLayoutReceptor_1;}
    public TextView getMensaje_2() {return mensaje_2;}
    public TextView getHora_2() {return hora_2;}
    public ImageView getFotoMensaje_2() {return fotoMensaje_2;}
    public RelativeLayout getLinearLayoutEmisor_2() {return linearLayoutEmisor_2;}

}