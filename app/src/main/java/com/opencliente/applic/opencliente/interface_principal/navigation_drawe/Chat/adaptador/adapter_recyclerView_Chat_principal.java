package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.adaptador;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_categoria_Negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.Chat.Chat_view;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Chat_principal extends RecyclerView.Adapter<adapter_recyclerView_Chat_principal.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_profile_negocio> pases;
    private View.OnClickListener listener;
    private Context context;

    // Firebase Data Base
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public adapter_recyclerView_Chat_principal(List<adapter_profile_negocio> business,Context context) {
        this.pases = business;
        this.context=context;
    }



    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, int position) {
        final adapter_profile_negocio ADP= pases.get(position);

        final Context context=holder.imageViewNotifiNewMessage.getContext();

        // BASE DE DATOS
        db.collection(context.getString(R.string.DB_NEGOCIOS)).document(ADP.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    adapter_profile_negocio adapter_perfilNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                    // Nombre del negocio
                    holder.dato1.setText(adapter_perfilNegocio.getNombre_negocio());

                    // Imagen del negocio
                    if (adapter_perfilNegocio.getImagen_perfil().equals("default")) {

                        //Asignacion de icono de la categoria
                        // Firebase DB categorias
                        FirebaseFirestore firestore_categoria=FirebaseFirestore.getInstance();
                        firestore_categoria.collection( context.getString(R.string.DB_APP) ).document( adapter_perfilNegocio.getPais().toUpperCase() ).collection( context.getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( adapter_perfilNegocio.getCategoria() )
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    if( documentSnapshot.exists() ){

                                        // adapter
                                        adapter_categoria_Negocio categoriaNegocio=documentSnapshot.toObject(adapter_categoria_Negocio.class);

                                        // Glide Descarga de imagen
                                        Glide.with(context.getApplicationContext())
                                                .load(categoriaNegocio.getLogo())
                                                .fitCenter()
                                                .centerCrop()
                                                .into(holder.circleImageView_perfil);


                                    }
                                }
                            }
                        });

                    }else{
                        //-Carga la imagen de perfil
                        Glide.with(context).load(adapter_perfilNegocio.getImagen_perfil()).into( holder.circleImageView_perfil);
                        }
                    // Estao mensnaje
                    if(ADP.getMensaje_nuevo().equals(true)){
                        holder.imageViewNotifiNewMessage.setVisibility(View.VISIBLE);
                    }else if(ADP.getMensaje_nuevo().equals(false)){
                        holder.imageViewNotifiNewMessage.setVisibility(View.GONE);
                    }

                    // Ultimo mensaje
                    holder.dato2.setText(ADP.getUltimo_mensaje());


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

        TextView dato1,dato2;
        CircleImageView circleImageView_perfil;
        ImageView imageViewNotifiNewMessage;



        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            circleImageView_perfil =(CircleImageView) itemView.findViewById(R.id.imageView_perfil_chat);
            imageViewNotifiNewMessage=(ImageView) itemView.findViewById(R.id.imageView_noti_nuevo_mensaje2);



        }
    }}