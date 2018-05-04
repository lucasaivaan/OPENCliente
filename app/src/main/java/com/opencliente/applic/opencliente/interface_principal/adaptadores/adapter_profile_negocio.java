package com.opencliente.applic.opencliente.interface_principal.adaptadores;

import android.graphics.drawable.Drawable;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by lucas on 21/10/2017.
 */

public class adapter_profile_negocio {


    public adapter_profile_negocio() {
    }

    String nombre_negocio;
    String pais;
    String direccion;
    String codigo_postal;
    String provincia;
    String ciudad;
    String telefono;
    String categoria;
    String sitio_web;
    String id;
    String descripcion;
    GeoPoint geopoint;

    String color="";
    String cardlayout="default";
    String imagen_perfil="default";

    Drawable icon;
    Boolean mensaje_nuevo=false; //el esta es por defecto "false"
    String ultimo_mensaje="";









    public String getUltimo_mensaje() { return ultimo_mensaje; }
    public void setUltimo_mensaje(String ultimo_mensaje) { this.ultimo_mensaje = ultimo_mensaje; }
    public String getImagen_perfil() { return imagen_perfil; }
    public void setImagen_perfil(String imagen_perfil) { this.imagen_perfil = imagen_perfil; }

    public String getCardlayout() { return cardlayout; }

    public void setCardlayout(String cardlayout) { this.cardlayout = cardlayout; }

    public Boolean getMensaje_nuevo() { return mensaje_nuevo; }

    public void setMensaje_nuevo(Boolean mensaje_nuevo) {
        this.mensaje_nuevo = mensaje_nuevo;
    }

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getNombre_negocio() {
        return nombre_negocio;
    }

    public String getPais() {
        return pais;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getSitio_web() {
        return sitio_web;
    }

    public void setNombre_negocio(String nombre_negocio) {
        this.nombre_negocio = nombre_negocio;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setSitio_web(String sitio_web) {
        this.sitio_web = sitio_web;
    }

}
