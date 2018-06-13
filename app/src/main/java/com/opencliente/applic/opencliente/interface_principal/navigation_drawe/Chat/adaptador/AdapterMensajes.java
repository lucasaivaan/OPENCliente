package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.MainActivity_lauch_Store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 04/09/2017. 04
 */

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    private View viewMensaje;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        viewMensaje = LayoutInflater.from(c).inflate(R.layout.recycler_mensaje_chat,parent,false);
        return new HolderMensaje(viewMensaje);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {

        //   1-Emisor  2-Receptor
        if(listMensaje.get(position).getType_mensaje().equals("1")){

            // Emisor
            holder.getMensaje_2().setText(listMensaje.get(position).getMensaje());

            holder.getLinearLayoutReceptor_1().setVisibility(View.GONE);

            // hora
            Date codigoHora = listMensaje.get(position).getTimestamp();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a  d MMM yyyy");//a pm o am
            holder.getHora_2().setText(sdf.format(codigoHora.getTime()));

        }else if(listMensaje.get(position).getType_mensaje().equals("2")){

            // Receptor


            // mensaje
            holder.getMensaje_1().setText(listMensaje.get(position).getMensaje());

            holder.getLinearLayoutEmisor_2().setVisibility(View.GONE);

            // hora
            Date codigoHora = listMensaje.get(position).getTimestamp();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a  d MMM yyyy");//a pm o am
            holder.getHora_1().setText(sdf.format(codigoHora.getTime()));

        }

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

}