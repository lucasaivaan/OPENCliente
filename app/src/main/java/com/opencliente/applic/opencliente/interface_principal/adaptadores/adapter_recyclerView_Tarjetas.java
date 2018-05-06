package com.opencliente.applic.opencliente.interface_principal.adaptadores;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Tarjetas extends RecyclerView.Adapter<adapter_recyclerView_Tarjetas.homeViwHolder>
        implements View.OnClickListener{

    private List<adapter_profile_negocio> pases;
    private View.OnClickListener listener;
    private Context context;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private adapter_profile_negocio adapterProfileNegocio;
    public adapter_recyclerView_Tarjetas(List<adapter_profile_negocio> business, Context context) {
        this.pases = business;
        this.context=context;
    }



    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_card_business,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, int position) {
        final adapter_profile_negocio ADP= pases.get(position);
        adapterProfileNegocio= new adapter_profile_negocio();
        holder.cardView.setVisibility(View.GONE);

        db.collection( context.getString(R.string.DB_NEGOCIOS) ).document(ADP.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){

                        // Adaptador perfil del negocip
                        adapterProfileNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                        holder.cardView.setVisibility(View.VISIBLE);

                        // Imagen de perfil del negocio
                        if(adapterProfileNegocio.getImagen_perfil().equals("default")){

                            int id= icono.getIconLogoCategoria(adapterProfileNegocio.getCategoria(),context);
                            adapterProfileNegocio.setIcon(context.getResources().getDrawable(id));//Asigna el icono correspondiente del negocio
                            holder.dato3.setImageDrawable(adapterProfileNegocio.getIcon());

                        }else{
                            Glide.clear(holder.dato3);
                            Context context=holder.dato3.getContext();
                            //-Carga la imagen de perfil
                            Glide.with(context).load( adapterProfileNegocio.getImagen_perfil() ).into( holder.dato3 );

                        }
                        // Color tarjeta
                        if(!adapterProfileNegocio.getColor().equals("")){
                            // Color del Cardview y TexView
                            holder.cardView.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
                        }

                        //stilo de tarjeta
                        Context context = holder.EstiloTarjeta.getContext();
                        if(adapterProfileNegocio.getCardlayout() !=null && adapterProfileNegocio.getCardlayout() != "") {

                            if (adapterProfileNegocio.getCardlayout().equals("default")) {
                                holder.EstiloTarjeta.setBackground(null);
                                // nombre color
                                holder.textView_nombre.setTextColor(Color.parseColor("#FFFFFF"));
                            } else {
                                int id = context.getResources().getIdentifier(adapterProfileNegocio.getCardlayout(), "mipmap", context.getPackageName());
                                holder.EstiloTarjeta.setBackgroundResource(id);

                                // nombre color
                                holder.textView_nombre.setTextColor(Color.parseColor(adapterProfileNegocio.getColor()));
                            }
                        }

                        // Nombre
                        holder.textView_nombre.setText(adapterProfileNegocio.getNombre_negocio());
                        // Ubicación
                        if(!adapterProfileNegocio.getDireccion().equals("")){
                            holder.textView_informacion.setText(adapterProfileNegocio.getDireccion());
                        }else{
                            holder.vista_ubicacion.setVisibility(View.GONE);
                        }
                        // Telefono
                        if(!adapterProfileNegocio.getTelefono().equals("")){
                            holder.textView_telefono.setText(adapterProfileNegocio.getTelefono());
                        }else{
                            holder.vista_telefono.setVisibility(View.GONE);
                        }
                        // Sitio web
                        if(!adapterProfileNegocio.getSitio_web().equals("")){
                            holder.textView_web.setText(adapterProfileNegocio.getSitio_web());
                        }else{
                            holder.vista_web.setVisibility(View.GONE);
                        }

                        // Estado de mensaje
                        if(ADP.getMensaje_nuevo() == true){
                            holder.imageViewNotifiNewMessage.setVisibility(View.VISIBLE);
                        }else if(ADP.getMensaje_nuevo() == false ){
                            holder.imageViewNotifiNewMessage.setVisibility(View.GONE);
                        }


                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return pases.size();  //devuelve el numero de fila que tiene el recycleview
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }}

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        TextView textView_nombre, textView_informacion,textView_telefono,textView_web;
        CircleImageView dato3;
        CardView cardView;
        //---ImageView
        ImageView imageViewNotifiNewMessage;
        LinearLayout EstiloTarjeta,vista_ubicacion,vista_telefono,vista_web;

        public homeViwHolder(View itemView) {
            super(itemView);

            textView_nombre =(TextView) itemView.findViewById(R.id.textView_nombre);
            textView_informacion =(TextView) itemView.findViewById(R.id.textView_info);
            textView_telefono =(TextView) itemView.findViewById(R.id.textView23);
            textView_web =(TextView) itemView.findViewById(R.id.textView28);
            dato3=(CircleImageView) itemView.findViewById(R.id.imageView_iconNegocio);
            imageViewNotifiNewMessage=(ImageView) itemView.findViewById(R.id.imageView_nuevo_mensaje);
            cardView=(CardView) itemView.findViewById(R.id.card_business);

            // Vistas
            EstiloTarjeta=(LinearLayout) itemView.findViewById(R.id.linealLayout_EstiloTarjeta);
            vista_ubicacion=(LinearLayout) itemView.findViewById(R.id.vista_ubicacion);
            vista_telefono=(LinearLayout) itemView.findViewById(R.id.vista_telefono);
            vista_web=(LinearLayout) itemView.findViewById(R.id.vista_web);



        }
    }}