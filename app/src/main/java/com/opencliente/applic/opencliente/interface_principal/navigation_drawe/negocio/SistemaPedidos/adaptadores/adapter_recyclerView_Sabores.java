package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.SistemaPedidos.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_producto;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_Sabores extends RecyclerView.Adapter<adapter_recyclerView_Sabores.homeViwHolder>
        implements View.OnClickListener{

    private final Context context;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    List<adapter_producto> List_Sabores;


    private View.OnClickListener listener;

    public adapter_recyclerView_Sabores(List<adapter_producto> productos, Context context) {
        this.context=context;
        this.List_Sabores = productos;
    }


    @Override
    public adapter_recyclerView_Sabores.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_productos,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Sabores.homeViwHolder holder=new adapter_recyclerView_Sabores.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, final int position) {
        final adapter_producto adapterProducto= List_Sabores.get(position);

        if(adapterProducto.getInfo1() != null){

            // Control de visivilidad
            holder.datoMarca.setVisibility(View.GONE);
            holder.datoPrecio.setVisibility(View.GONE);

            // nombre del sabor
            holder.datoInfo.setText( adapterProducto.getInfo2() );

            // imagen
            if(!adapterProducto.getUrlimagen().equals("default")){
                Glide.with(context)
                        .load(adapterProducto.getUrlimagen())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.ImageProducto);
            }


           if(  List_Sabores.get(position).getClickalble() == true  ){
                // set Color
                holder.cardViewColor.setCardBackgroundColor(  Color.parseColor("#a5d6a7")  );

            }else if( List_Sabores.get(position).getClickalble() == false ){
                // Set color
               holder.cardViewColor.setCardBackgroundColor(  Color.parseColor("#FFFFFFFF")  );

            }




        }


    }


    @Override
    public int getItemCount() {
        return List_Sabores.size();  //devuelve el numero de fila que tiene el recycleview
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

        TextView datoMarca,datoInfo,datoPrecio;
        CircleImageView ImageProducto;
        ImageView imageViewTilde;
        CardView cardViewColor;

        public homeViwHolder(View itemView) {
            super(itemView);

            datoMarca=(TextView) itemView.findViewById(R.id.textView_marca);
            datoInfo=(TextView) itemView.findViewById(R.id.textView_info);
            datoPrecio=(TextView) itemView.findViewById(R.id.textView_precio);
            ImageProducto=(CircleImageView) itemView.findViewById(R.id.imageView_producto);
            imageViewTilde=(ImageView) itemView.findViewById(R.id.imageView15_tilde);
            cardViewColor=(CardView) itemView.findViewById(R.id.carview_producto);



        }
    }



}