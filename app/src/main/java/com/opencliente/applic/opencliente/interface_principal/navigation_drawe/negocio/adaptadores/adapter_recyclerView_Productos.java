package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencliente.applic.opencliente.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_Productos extends RecyclerView.Adapter<adapter_recyclerView_Productos.homeViwHolder>
        implements View.OnClickListener{

    private final Context context;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    List<adapter_producto> adapter_productoList;


    private View.OnClickListener listener;

    public adapter_recyclerView_Productos(List<adapter_producto> productos, Context context) {
        this.context=context;
        this.adapter_productoList = productos;
    }


    @Override
    public adapter_recyclerView_Productos.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_productos,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Productos.homeViwHolder holder=new adapter_recyclerView_Productos.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, int position) {
        final adapter_producto adapterProducto= adapter_productoList.get(position);

        if(adapterProducto.getInfo1() != null){
            holder.datoMarca.setText(adapterProducto.getInfo1());
            holder.datoInfo.setText(adapterProducto.getInfo2());

            if(adapterProducto.getPrecio() ==null){
                holder.datoPrecio.setVisibility(View.GONE);
            }else {
                holder.datoPrecio.setText("$"+String.valueOf(adapterProducto.getPrecio()));

            }

            if(!adapterProducto.getUrlimagen().equals("default")){
                Glide.with(context)
                        .load(adapterProducto.getUrlimagen())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.ImageProducto);
            }else{
                holder.progressBar.setVisibility(View.GONE);


            }
        }
    }


    @Override
    public int getItemCount() {
        return adapter_productoList.size();  //devuelve el numero de fila que tiene el recycleview
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        TextView datoMarca,datoInfo,datoPrecio;
        CircleImageView ImageProducto;
        ImageView imageViewTilde;
        CardView cardView;
        ProgressBar progressBar;

        public homeViwHolder(View itemView) {
            super(itemView);

            datoMarca=(TextView) itemView.findViewById(R.id.textView_marca);
            datoInfo=(TextView) itemView.findViewById(R.id.textView_info);
            datoPrecio=(TextView) itemView.findViewById(R.id.textView_precio);
            ImageProducto=(CircleImageView) itemView.findViewById(R.id.imageView_producto);
            imageViewTilde=(ImageView) itemView.findViewById(R.id.imageView15_tilde);
            cardView=(CardView) itemView.findViewById(R.id.carview_producto);
            progressBar=(ProgressBar) itemView.findViewById(R.id.progressBar2);


        }
    }



}