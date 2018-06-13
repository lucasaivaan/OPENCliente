package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_categoria_Negocio;
import com.opencliente.applic.opencliente.interface_principal.adaptadores.adapter_profile_negocio;
import com.opencliente.applic.opencliente.interface_principal.metodos_funciones.icono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_lista_pedidos extends RecyclerView.Adapter<adapter_recyclerView_lista_pedidos.homeViwHolder>
        implements View.OnClickListener{

    // FIREBASE
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    List<adaptador_pedido> ListPedidos;
    private Context context;

    private View.OnClickListener listener;

    public adapter_recyclerView_lista_pedidos(List<adaptador_pedido> ListDirecciones,Context context) {
        this.ListPedidos = ListDirecciones;
        this.context = context;
    }


    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_pedidos,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, final int position) {
        final adaptador_pedido ADP= ListPedidos.get(position);

        if(ADP.getEstado() !=4){

            firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ADP.getId_negocio() ).collection(  context.getResources().getString(R.string.DB_PEDIDOS)  ).document(ADP.getId())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            if(documentSnapshot.exists()){
                                // Adaptador
                                adaptador_pedido adaptadorPedido=documentSnapshot.toObject(  adaptador_pedido.class  );
                                ListPedidos.get(position).setEstado(adaptadorPedido.getEstado());

                                // Tipo de entrega   1=Retira personalmente  2=Delivery
                                if( adaptadorPedido.getTipo_entrega() == 1){
                                    holder.tTipoEntrega.setText(  context.getResources().getString(R.string.retiro_en_el_negocio)  );
                                }else if( adaptadorPedido.getTipo_entrega() == 2){
                                    holder.tTipoEntrega.setText(  context.getResources().getString(R.string.delivery)  );
                                }

                                // Hora de retiro en el negocio
                                holder.textView_HoraRetiro.setText( adaptadorPedido.getHora() );

                                // Estado del pedido
                                if(adaptadorPedido.getEstado() != null ){

                                    switch (adaptadorPedido.getEstado()){
                                        case 0:
                                            holder.tEstado.setText("Pendiente");
                                            holder.tEstado.setBackgroundResource(R.color.md_deep_orange_400);
                                            break;
                                        case 1:
                                            holder.tEstado.setText("En proceso");
                                            holder.tEstado.setBackgroundResource(R.color.md_teal_300);
                                            break;
                                        case 2:
                                            holder.tEstado.setText("Enviado a su direccion");
                                            holder.tEstado.setBackgroundResource(R.color.md_light_green_400);
                                            break;
                                        case 3:
                                            holder.tEstado.setText("Pedido listo paras retirar del local");
                                            holder.tEstado.setBackgroundResource(R.color.md_light_green_400);
                                            break;

                                        case 4:
                                            holder.tEstado.setText("Cancelado");
                                            holder.tEstado.setBackgroundResource(R.color.md_red_400);
                                            break;
                                        case 5:
                                            holder.tEstado.setText("Recibido");
                                            holder.tEstado.setBackgroundResource(R.color.md_light_green_500);
                                            break;

                                    }

                                }

                                // Cantidad del producto
                                Map<String, Object> map = adaptadorPedido.getLista_productos();
                                holder.tCantidadProducto.setText( context.getResources().getString(R.string.productos) +" "+ String.valueOf(map.size()) );


                                // hora
                                Date codigoHora = adaptadorPedido.getTimestamp();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");//a pm o am
                                holder.tHora.setText(sdf.format(codigoHora.getTime()));

                            }
                        }
                    });

            DocumentReference documentReference=firestore.collection( context.getString(R.string.DB_NEGOCIOS) ).document(ADP.getId_negocio());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(documentSnapshot.exists()){

                            // Adaptador perfil del negocip
                            adapter_profile_negocio adapterProfileNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                            // Imagen de perfil del negocio
                            if(adapterProfileNegocio.getImagen_perfil().equals("default")){

                                //Asignacion de icono de la categoria
                                // Firebase DB categorias
                                FirebaseFirestore firestore_categoria=FirebaseFirestore.getInstance();
                                firestore_categoria.collection( context.getString(R.string.DB_APP) ).document( adapterProfileNegocio.getPais().toUpperCase() ).collection( context.getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( adapterProfileNegocio.getCategoria() )
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
                                                        .into(holder.profile_image);


                                            }
                                        }
                                    }
                                });

                            }else{
                                Glide.clear(holder.profile_image);
                                Context context=holder.profile_image.getContext();
                                //-Carga la imagen de perfil
                                Glide.with(context).load( adapterProfileNegocio.getImagen_perfil() ).into( holder.profile_image );

                            }


                        }
                    }

                }
            });

        }else if(ADP.getEstado() ==4){

            holder.tEstado.setText("Cancelado");
            holder.tEstado.setBackgroundResource(R.color.md_red_400);

            holder.tCantidadProducto.setText(ADP.getMensaje());
            holder.tTipoEntrega.setText("");
            holder.tHora.setText("");


            DocumentReference documentReference=firestore.collection( context.getString(R.string.DB_NEGOCIOS) ).document(ADP.getId_negocio());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(documentSnapshot.exists()){

                            // Adaptador perfil del negocip
                            adapter_profile_negocio adapterProfileNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                            // Imagen de perfil del negocio
                            if(adapterProfileNegocio.getImagen_perfil().equals("default")){

                                int id= icono.getIconLogoCategoria(adapterProfileNegocio.getCategoria(),context);
                                adapterProfileNegocio.setIcon(context.getResources().getDrawable(id));//Asigna el icono correspondiente del negocio
                                holder.profile_image.setImageDrawable(adapterProfileNegocio.getIcon());

                            }else{
                                Glide.clear(holder.profile_image);
                                Context context=holder.profile_image.getContext();
                                //-Carga la imagen de perfil
                                Glide.with(context).load( adapterProfileNegocio.getImagen_perfil() ).into( holder.profile_image );

                            }


                        }
                    }

                }
            });

        }





    }




    @Override
    public int getItemCount() {
        return ListPedidos.size();  //devuelve el numero de fila que tiene el recycleview
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

        CircleImageView profile_image;
        TextView tTipoEntrega,tCantidadProducto,tHora,tEstado,textView_HoraRetiro;

        public homeViwHolder(View itemView) {
            super(itemView);

            tTipoEntrega=(TextView) itemView.findViewById(R.id.textView39);
            tCantidadProducto=(TextView) itemView.findViewById(R.id.textView42);
            tHora=(TextView) itemView.findViewById(R.id.textView41);
            tEstado=(TextView) itemView.findViewById(R.id.textView_estado);
            textView_HoraRetiro=(TextView) itemView.findViewById(R.id.textView_HoraRetiro);


            profile_image=(CircleImageView) itemView.findViewById(R.id.profile_image);



        }
    }}