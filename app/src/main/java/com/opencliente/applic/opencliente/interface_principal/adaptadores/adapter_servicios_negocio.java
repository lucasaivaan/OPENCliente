package com.opencliente.applic.opencliente.interface_principal.adaptadores;

import android.graphics.drawable.Drawable;

/**
 * Created by lucas on 24/10/2017.
 */

public class adapter_servicios_negocio {
    public adapter_servicios_negocio() {
    }

    String titulo;
    String descripcion;
    Drawable ic_servico;
    String ic_name;


    public String getIc_name() {
        return ic_name;
    }

    public void setIc_name(String ic_name) {
        this.ic_name = ic_name;
    }



    public Drawable getIc_servico() {
        return ic_servico;
    }

    public void setIc_servico(Drawable ic_servico) {
        this.ic_servico = ic_servico;
    }



    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }



}
