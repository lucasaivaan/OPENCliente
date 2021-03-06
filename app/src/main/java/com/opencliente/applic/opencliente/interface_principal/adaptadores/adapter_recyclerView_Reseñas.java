package com.opencliente.applic.opencliente.interface_principal.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ivan on 15/3/2018.
 */

public class adapter_recyclerView_Reseñas extends RecyclerView.Adapter<adapter_recyclerView_Reseñas.homeViwHolder>
        implements View.OnClickListener{


    List<adapter_review> adapter_reviews;
    protected adapter_review adapterReseña;
    private Context context;
    private View.OnClickListener listener;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public adapter_recyclerView_Reseñas(List<adapter_review> List_business , Context context) {
        this.adapter_reviews = List_business;
        this.context=context;
    }



    @Override
    public adapter_recyclerView_Reseñas.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_reviews,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Reseñas.homeViwHolder holder=new adapter_recyclerView_Reseñas.homeViwHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final adapter_recyclerView_Reseñas.homeViwHolder holder, int position) {
        adapterReseña= adapter_reviews.get(position);

        firestore.collection( context.getResources().getString(R.string.DB_CLIENTES) ).document( adapterReseña.getId() )
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc_perfilUsuario=task.getResult();
                    if(doc_perfilUsuario.exists()){
                        adapter_profile_clientes adapterProfileClientes=doc_perfilUsuario.toObject(adapter_profile_clientes.class);

                        // Imagen de perfil
                        Glide.clear(holder.circleImageView);
                        // foto perfil
                        if(!adapterProfileClientes.getUrlfotoPerfil().equals("default")){

                            // Descarga la imagen de internet
                            try {
                                Context context = holder.circleImageView.getContext();
                                Glide.with(context).load(adapterProfileClientes.getUrlfotoPerfil())
                                        .fitCenter()
                                        .centerCrop()
                                        .into( holder.circleImageView);
                            }catch (Exception ex){ holder.circleImageView.setBackgroundResource(R.mipmap.ic_user2);}

                        }else {
                            // Asigna una imagen por defecto
                            holder.circleImageView.setBackgroundResource(R.mipmap.ic_user2);
                        }

                        // Nombre
                        holder.dato1.setText(adapterProfileClientes.getNombre());

                    }
                }
            }
        });

        try{

            // Reseña
            holder.dato2.setText(adapterReseña.getReseña());

            //fecha
            Date adapterReseñaTimestamp = adapterReseña.getTimestamp();
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

            // hora
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
            holder.dato3.setText(sdf.format(adapterReseñaTimestamp.getTime())+" "+format1.format(adapterReseñaTimestamp.getTime()));

            // Reseña positiva o negativa
            if(adapterReseña.getEstrellas() != null){

                // Reseña positiva
                holder.ratingBarStat.setRating(adapterReseña.getEstrellas());

            }else{ holder.ratingBarStat.setVisibility(View.GONE); }

        }catch (Exception ex){}
    }

    @Override
    public int getItemCount() {
        return adapter_reviews.size();  //devuelve el numero de fila que tiene el recycleview
    }


    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }


    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        TextView dato1,dato2,dato3;
        CircleImageView circleImageView;
        RatingBar ratingBarStat;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textview_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textview_reseña);
            dato3=(TextView) itemView.findViewById(R.id.textview_hora);
            circleImageView=(CircleImageView) itemView.findViewById(R.id.fotoPerfilReseña);
            ratingBarStat=(RatingBar) itemView.findViewById(R.id.rationbar_star);

        }
    }}